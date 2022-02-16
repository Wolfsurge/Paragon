package com.paragon.client.features.module.impl.combat;

import com.paragon.api.event.events.UpdateEvent;
import com.paragon.api.event.events.network.PacketEvent;
import com.paragon.api.util.player.InventoryUtil;
import com.paragon.api.util.player.RotationUtil;
import com.paragon.api.util.render.ColourUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.api.util.world.BlockUtil;
import com.paragon.api.util.world.EntityUtil;
import com.paragon.asm.mixins.accessor.ICPacketUseEntity;
import com.paragon.client.Paragon;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ColourSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import com.paragon.client.managers.rotation.Rotation;
import com.paragon.client.managers.rotation.RotationType;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Don't skid this, it's not the best.
 * @author Wolfsurge
 * @since 31/01/22
 */
public class AutoCrystal extends Module {

    // Targeting
    private final BooleanSetting targeting = new BooleanSetting("Targeting", "The targeting settings", false);
    private final NumberSetting targetRange = (NumberSetting) new NumberSetting("Target Range", "How far to find targets", 7, 1, 15, 1).setParentSetting(targeting);
    private final ModeSetting targetPriority = (ModeSetting) new ModeSetting("Target Priority", "The mode to find targets", "Distance", new String[]{"Distance", "Health"}).setParentSetting(targeting);
    private final BooleanSetting reverseList = (BooleanSetting) new BooleanSetting("Reverse Target List", "Reverse the targets list, to find the player with the highest value", false).setParentSetting(targeting);

    // Placing
    private final BooleanSetting place = new BooleanSetting("Place", "Automatically place crystals", true);
    private final ModeSetting when = (ModeSetting) new ModeSetting("When", "When to place crystals", "Holding", new String[]{"Holding", "Switch", "Silent Switch"}).setParentSetting(place);
    private final NumberSetting placeRange = (NumberSetting) new NumberSetting("Place Range", "The limit on how far away you place crystals", 5, 1, 9, 1).setParentSetting(place);
    private final ModeSetting placeRotation = (ModeSetting) new ModeSetting("Rotate", "How to rotate when placing the crystal", "Packet", new String[]{"Packet", "Legit", "None"}).setParentSetting(place);
    private final NumberSetting placeCooldown = (NumberSetting) new NumberSetting("Place Cooldown", "How long to cool down for", 0, 0, 80, 1).setParentSetting(place);

    // Breaking
    private final BooleanSetting breakCrystals = new BooleanSetting("Break", "Break crystals around you", true);
    private final NumberSetting breakRange = (NumberSetting) new NumberSetting("Break Range", "The range in which to break crystals", 5, 2, 8, 1).setParentSetting(breakCrystals);
    private final ModeSetting breakLogic = (ModeSetting) new ModeSetting("Break Logic", "Which crystals to break", "Smart", new String[]{"All", "Self", "Smart", "Self Smart"}).setParentSetting(breakCrystals);
    private final BooleanSetting instant = (BooleanSetting) new BooleanSetting("Instant", "Instantly hit crystals", true).setParentSetting(breakCrystals);
    private final BooleanSetting swing = (BooleanSetting) new BooleanSetting("Swing", "Swing your hand when you break a crystal", true).setParentSetting(breakCrystals);
    private final ModeSetting breakRotation = (ModeSetting) new ModeSetting("Rotate", "How to rotate when breaking crystals", "Packet", new String[]{"Packet", "Legit", "None"}).setParentSetting(breakCrystals);
    private final BooleanSetting breakSwing = (BooleanSetting) new BooleanSetting("Swing", "Swing the player's hand when breaking a crystal", true).setParentSetting(breakCrystals);
    private final ModeSetting antiWeakness = (ModeSetting) new ModeSetting("Anti Weakness", "Lets you be able to break crystals whilst under the weakness effect", "Off", new String[]{"Off", "Switch", "Silent Switch"}).setParentSetting(breakCrystals);

    // Other
    private final BooleanSetting other = new BooleanSetting("Other", "Other settings", false);
    private final ModeSetting updateEvent = (ModeSetting) new ModeSetting("Update", "When to call the update", "Client", new String[]{"Client", "Server", "Both"}).setParentSetting(other);
    private final ModeSetting logic = (ModeSetting) new ModeSetting("Logic", "Whether to break or place crystals before the other", "Place -> Break", new String[]{"Break -> Place", "Place -> Break"}).setParentSetting(other);
    private final NumberSetting minimumDamage = (NumberSetting) new NumberSetting("Minimum Damage", "The minimum amount of damage to do to the target", 4, 0, 36, 1).setParentSetting(other);
    private final NumberSetting maximumLocalDamage = (NumberSetting) new NumberSetting("Maximum Local Damage", "The maximum amount of damage to inflict upon yourself", 6, 1, 20, 1).setParentSetting(other);

    // HUD Info
    private final BooleanSetting HUD = new BooleanSetting("HUD", "HUD Settings for the module", false);
    private final ModeSetting hudInfo = (ModeSetting) new ModeSetting("HUD Info", "What text to display on the Array List", "Target Info", new String[]{"Target Info", "Crystal Count"}).setParentSetting(HUD);
    private final BooleanSetting targetHealth = (BooleanSetting) new BooleanSetting("Target Health", "Show target health in the Array List", true).setParentSetting(HUD);

    // Render
    private final BooleanSetting render = new BooleanSetting("Render", "Render an outline around the best block", true);
    private final ColourSetting colour = (ColourSetting) new ColourSetting("Colour", "The colour of the outline", new Color(185, 19, 211)).setParentSetting(render);
    private final ModeSetting renderMode = (ModeSetting) new ModeSetting("Mode", "The mode for rendering", "Both", new String[]{"Both", "Outline", "Fill"}).setParentSetting(render);
    private final NumberSetting lineWidth = (NumberSetting) new NumberSetting("Line Width", "Width of the outline", 1, 0.1f, 2, 0.1f).setParentSetting(render);

    // Variables
    private EntityPlayer currentTarget; // The current player being targeted
    private final ArrayList<BlockPos> selfPlacedCrystals = new ArrayList<>(); // List of self placed crystals

    private int placeCooldownCount = 0;

    public AutoCrystal() {
        super("AutoCrystal", "Automatically places and breaks crystals", Category.COMBAT);
        this.addSettings(targeting, place, breakCrystals, other, HUD, render);
    }

    @Override
    public void onEnable() {
        resetVariables();
    }

    @Override
    public void onDisable() {
        resetVariables();
    }

    @EventHandler
    private final Listener<UpdateEvent> updateEventListener = new Listener<>(event -> {
        if(updateEvent.is("Client") && event.getUpdateType() != UpdateEvent.Type.Client || updateEvent.is("Server") && event.getUpdateType() != UpdateEvent.Type.Server) return;

        doAutoCrystal();
    });

    public void doAutoCrystal() {
        if(!setTarget()) return;

        switch (logic.getCurrentMode()) {
            case "Place -> Break":
                placeCrystals();
                breakCrystals();
                break;
            case "Break -> Place":
                breakCrystals();
                placeCrystals();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (currentTarget == null || !render.isEnabled()) return;

        if (getBestPosition(currentTarget) != null) {
            BlockPos pos = getBestPosition(currentTarget);

            if (renderMode.is("Fill") || renderMode.is("Both")) {
                RenderUtil.drawFilledBox(BlockUtil.getBlockBox(pos), ColourUtil.integrateAlpha(colour.getColour(), 180 / 255f));
            }

            if (renderMode.is("Outline") || renderMode.is("Both")) {
                RenderUtil.drawBoundingBox(BlockUtil.getBlockBox(pos), lineWidth.getValue(), colour.getColour());
            }
        }
    }

    public void breakCrystals() {
        if(breakCrystals.isEnabled()) {
            mc.world.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityEnderCrystal) {
                    EntityEnderCrystal crystal = (EntityEnderCrystal) entity;

                    if (canBreakCrystal(crystal, currentTarget) && isCrystalInRange(crystal)) {
                        breakCrystal(crystal);
                    }
                }
            });
        }
    }

    public void placeCrystals() {
        if(place.isEnabled()) {
            placeCooldownCount--;

            if (placeCooldownCount > 0) {
                return;
            }

            if (placeCooldownCount == 0) {
                placeCooldownCount = (int) placeCooldown.getValue();
            }

            if(!prePlace()) return;

            if (getBestPosition(currentTarget) != null) {
                placeCrystalOnBlock(getBestPosition(currentTarget), !placeRotation.is("None"));
            }
        }
    }

    @EventHandler
    private final Listener<PacketEvent.PreReceive> preReceiveListener = new Listener<>(event -> {
        // Check that we want to instant break, and we have a target
        if (instant.isEnabled() && currentTarget != null) {
            if (event.getPacket() instanceof SPacketSpawnObject) {
                SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();
                BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());

                if (packet.getType() == 51 && selfPlacedCrystals.contains(pos.down())) {
                    // Cancel if the position is too far away
                    if (mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()) > breakRange.getValue()) {
                        return;
                    }

                    // Cancel if the damage is less than the minimum damage
                    if ((breakLogic.is("Smart") || breakLogic.is("Self Smart") || breakLogic.is("All Smart")) && calculateCrystalDamage(pos, currentTarget) <= minimumDamage.getValue()) {
                        return;
                    }

                    // Cancel if the damage to the player is higher than max local
                    if ((breakLogic.is("Smart") || breakLogic.is("Self Smart") || breakLogic.is("All Smart")) && calculateCrystalDamage(pos, mc.player) >= maximumLocalDamage.getValue()) {
                        return;
                    }

                    instantHit(packet.getEntityID());
                }
            }
        }
    });

    /* Place Utils */

    /**
     * Gets the best position to place
     * @param target The target
     * @return The best position to place
     */
    public BlockPos getBestPosition(Entity target) {
        BlockPos currentBest = null;

        for (BlockPos pos : getPlaceableBlocks(target)) {
            // If currentBest is null, set this position to currentBest
            if (currentBest == null || calculateCrystalDamage(pos, target) > calculateCrystalDamage(currentBest, target)) {
                currentBest = pos;
            }
        }

        return currentBest;
    }

    /**
     * Gets a list of all placeable blocks
     * @param target The target
     * @return A list of all placeable blocks
     */
    public ArrayList<BlockPos> getPlaceableBlocks(Entity target) {
        ArrayList<BlockPos> availablePositions = new ArrayList<>();
        for(BlockPos pos : BlockUtil.getBlocksAroundPlayer(mc.player, placeRange.getValue())) {
            if(canPlaceCrystalOnBlock(pos) && isWithinDamageParameters(pos, target))
                availablePositions.add(pos);
        }
        return availablePositions;
    }

    /**
     * Checks if the player can place a crystal on the block
     * @param pos The block to check
     * @return Whether the block is placeable
     */
    public boolean canPlaceCrystalOnBlock(BlockPos pos) {
        return (BlockUtil.getBlockAtPos(pos) == Blocks.OBSIDIAN || BlockUtil.getBlockAtPos(pos) == Blocks.BEDROCK) // Block check
                && BlockUtil.getBlockAtPos(pos.up()) == Blocks.AIR // Block above check
                && BlockUtil.getBlockAtPos(pos.up().up()) == Blocks.AIR // Block above that check
                && !BlockUtil.isIntercepted(pos) // Checks if there is an entity in the way
                ;
    }

    /**
     * Gets the best player to target
     * @return The best player to target
     */
    public EntityPlayer getBestTarget() {
        // Get entity list
        Stream<Entity> entityStream = mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityOtherPlayerMP && entity.getDistance(mc.player) <= targetRange.getValue());
        // Turn into list
        List<Entity> playerEntities = entityStream.collect(Collectors.toList());

        // Sort
        if(targetPriority.is("Closest")) playerEntities.sort(Comparator.comparingDouble(entity -> entity.getDistance(mc.player)));
        else if(targetPriority.is("Health")) playerEntities.sort(Comparator.comparingDouble(entity -> ((EntityOtherPlayerMP) entity).getHealth()));

        // Reverse list
        if(!reverseList.isEnabled())
            Collections.reverse(playerEntities);

        // If the list isn't empty, return the best player
        if(!playerEntities.isEmpty()) {
            return (EntityPlayer) playerEntities.get(0);
        }

        else return null;
    }

    /**
     * Gets whether the player should place crystals
     * @return Whether the player should place crystals
     */
    public boolean shouldPlace() {
        return InventoryUtil.isHolding(Items.END_CRYSTAL) && when.is("Holding");
    }

    /**
     * The code to run before placing
     * @return Whether placing is cancelled
     */
    public boolean prePlace() {
        if(shouldPlace())
            return true;
        else if(when.is("Switch") || when.is("Silent Switch")) {
            if(InventoryUtil.switchToItem(Items.END_CRYSTAL, when.is("Silent Switch"))) return true;
        }

        return false;
    }

    /**
     * Places a crystal on a block
     * @param pos The position to place on
     * @param rotate Whether to rotate or not
     */
    public void placeCrystalOnBlock(BlockPos pos, boolean rotate) {
        if (rotate) {
            Paragon.rotationManager.addRotation(new Rotation(RotationUtil.getRotationToBlockPos(pos).x, RotationUtil.getRotationToBlockPos(pos).y, RotationType.valueOf(placeRotation.getCurrentMode().toUpperCase())));
        }

        mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.getDirectionFromEntityLiving(pos, mc.player), getHandHoldingCrystals(), 0, 0, 0));

        // Add the block position above the place position, as that is where the crystals position will be
        selfPlacedCrystals.add(pos.up());
    }

    /**
     * Gets the hand which is holding crystals
     * @return The hand which is holding crystals
     */
    public EnumHand getHandHoldingCrystals() {
        if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL)
            return EnumHand.MAIN_HAND;
        else if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)
            return EnumHand.OFF_HAND;
        else
            return null;
    }

    /* Break Utils */

    /**
     * Checks if we can break the crystal
     * @param entityEnderCrystal The crystal to check
     * @param target The target
     * @return If we can break the crystal
     */
    public boolean canBreakCrystal(EntityEnderCrystal entityEnderCrystal, Entity target) {
        switch (breakLogic.getCurrentMode()) {
            // Smart - Crystal damage is less than or equal to maximum local damage, and greater than or equal to minimum damage
            case "Smart":
                return isWithinDamageParameters(entityEnderCrystal.getPosition(), target);
            // All - Breaks all crystals
            case "All":
                return true;
            // Self - Only breaks crystals we have placed
            case "Self":
                return selfPlacedCrystals.contains(entityEnderCrystal.getPosition());
            // Self Smart - Breaks crystals that we have placed, and meet the requirements for 'Smart'
            case "Self Smart":
                return isWithinDamageParameters(entityEnderCrystal.getPosition(), target) && selfPlacedCrystals.contains(entityEnderCrystal.getPosition());
        }

        return false;
    }

    /**
     * Is the crystal in range of the player
     * @param entityEnderCrystal The crystal
     * @return Whether the crystal in range of the player
     */
    public boolean isCrystalInRange(EntityEnderCrystal entityEnderCrystal) {
        return entityEnderCrystal.getDistance(mc.player) <= breakRange.getValue();
    }

    public void breakCrystal(EntityEnderCrystal crystal) {
        int antiWeaknessOldSlot = mc.player.inventory.currentItem;

        if (!antiWeakness.is("Off")) {
            // Weakness
            PotionEffect weakness = mc.player.getActivePotionEffect(MobEffects.WEAKNESS);
            // Strength
            PotionEffect strength = mc.player.getActivePotionEffect(MobEffects.STRENGTH);

            // Switch if we cannot break the crystal with our hands
            if (weakness != null && (strength == null || strength.getAmplifier() < weakness.getAmplifier())) {
                InventoryUtil.switchToItem(Items.DIAMOND_SWORD, antiWeakness.is("Silent Switch"));
            }
        }

        // Rotate to crystal
        Vec2f rotation = RotationUtil.getRotationToVec3d(new Vec3d(crystal.posX + 0.5f, crystal.posY + 0.5f, crystal.posZ + 0.5f));
        new Rotation(rotation.x, rotation.y, RotationType.valueOf(breakRotation.getCurrentMode().toUpperCase())).doRotation();

        if (instant.isEnabled()) {
            // Send a CPacketUseEntity to instant break the crystal
            mc.getConnection().sendPacket(new CPacketUseEntity(crystal));
        } else {
            // Attack the entity
            mc.playerController.attackEntity(mc.player, crystal);
        }

        if (breakSwing.isEnabled()) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }

        if (!antiWeakness.is("Off")) {
            // Switch back
            mc.getConnection().sendPacket(new CPacketHeldItemChange(antiWeaknessOldSlot));
        }
    }

    public void instantHit(int id) {
        ICPacketUseEntity hitPacket = (ICPacketUseEntity) (new CPacketUseEntity());
        hitPacket.setEntityId(id);
        hitPacket.setAction(CPacketUseEntity.Action.ATTACK);
        mc.getConnection().sendPacket((Packet<?>) hitPacket);

        if (swing.isEnabled()) {
            mc.player.swingArm(getHandHoldingCrystals());
        }
    }

    /* Other Utils */

    /**
     * Checks if the position's damage is less than maximum local and bigger than minimum
     * @param pos The position
     * @param target The target
     * @return Whether the position's damage is less than maximum local and bigger than minimum
     */
    public boolean isWithinDamageParameters(BlockPos pos, Entity target) {
        return calculateCrystalDamage(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, target) >= minimumDamage.getValue() / 36.0f && calculateCrystalDamage(pos, mc.player) <= maximumLocalDamage.getValue() / 36.0f;
    }

    /**
     * Resets the variables
     */
    public void resetVariables() {
        this.currentTarget = null;
        this.selfPlacedCrystals.clear();
    }

    /**
     * Sets the target
     * @return Whether the target has been set
     */
    public boolean setTarget() {
        currentTarget = getBestTarget();

        return currentTarget != null;
    }

    /**
     * Calculates crystal damage from given positions
     * @param posX The X position
     * @param posY The Y position
     * @param posZ The Z position
     * @param entity The entity to calculate damage for
     * @return The damage
     */
    public float calculateCrystalDamage(double posX, double posY, double posZ, Entity entity) {
        double factor = (1.0 - entity.getDistance(posX, posY, posZ) / 12.0) * entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());

        float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0f * 7.0f * 12.0f + 1.0f);

        double damage = 1.0;

        if (entity instanceof EntityLivingBase) {
            damage = getBlastReduction((EntityLivingBase) entity, calculatedDamage * ((mc.world.getDifficulty().getDifficultyId() == 0) ? 0.0f : ((mc.world.getDifficulty().getDifficultyId() == 2) ? 1.0f : ((mc.world.getDifficulty().getDifficultyId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, entity, posX, posY, posZ, 6.0f, false, true));
        }

        return (float) damage;
    }

    /**
     * Gets the blast reduction of a crystal explosion
     * @param entityLivingBase The entity to calculate damage for
     * @param damage The given damage
     * @param explosion The explosion to calculate
     * @return The blast reduction
     */
    public float getBlastReduction(EntityLivingBase entityLivingBase, float damage, Explosion explosion) {
        if (entityLivingBase instanceof EntityPlayer) {
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) entityLivingBase.getTotalArmorValue(), (float) entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            damage *= 1.0f - MathHelper.clamp((float) EnchantmentHelper.getEnchantmentModifierDamage(entityLivingBase.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)), 0.0f, 20.0f) / 25.0f;

            if (entityLivingBase.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }

            return damage;
        }

        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entityLivingBase.getTotalArmorValue(), (float) entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

        return damage;
    }

    /**
     * Calculates crystal damage from a block position
     * @param pos The given position
     * @param entity The entity to calculate damage for
     * @return The calculated damage
     */
    public float calculateCrystalDamage(BlockPos pos, Entity entity) {
        return calculateCrystalDamage(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, entity);
    }

    /**
     * Gets the amount of crystals in the player's hotbar
     * @return The amount of crystals in the player's hotbar
     */
    public int getCrystalCount() {
        int crystals = 0;
        for(int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if(itemStack.getItem() == Items.END_CRYSTAL)
                crystals += itemStack.getCount();
        }
        return crystals;
    }

    /**
     * Builds module info for the Array List
     * @return The module info
     */
    public String buildModuleInfo() {
        String moduleInfo = "";
        if(hudInfo.is("Target Info"))
            moduleInfo = " " + (currentTarget == null ? "No Target" : currentTarget.getName() + (targetHealth.isEnabled() ? " " + EntityUtil.getTextColourFromEntityHealth(currentTarget) + currentTarget.getHealth() : ""));
        else if(hudInfo.is("Crystal Count"))
            moduleInfo = " " + getCrystalCount();

        return moduleInfo;
    }

    /**
     * Gets info for the Array List
     * @return Info for the Array List
     */
    @Override
    public String getModuleInfo() {
        return buildModuleInfo();
    }
}