package com.paragon.client.systems.module.impl.combat;

import com.paragon.Paragon;
import com.paragon.api.event.client.SettingUpdateEvent;
import com.paragon.api.event.network.PacketEvent;
import com.paragon.api.util.calculations.MathsUtil;
import com.paragon.api.util.calculations.Timer;
import com.paragon.api.util.entity.EntityUtil;
import com.paragon.api.util.player.InventoryUtil;
import com.paragon.api.util.player.PlayerUtil;
import com.paragon.api.util.player.RotationUtil;
import com.paragon.api.util.render.ColourUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.api.util.world.BlockUtil;
import com.paragon.asm.mixins.accessor.IPlayerControllerMP;
import com.paragon.client.managers.rotation.Rotate;
import com.paragon.client.managers.rotation.Rotation;
import com.paragon.client.managers.rotation.RotationPriority;
import com.paragon.api.module.Module;
import com.paragon.api.module.Category;
import com.paragon.client.systems.module.impl.misc.AutoEZ;
import com.paragon.api.setting.Bind;
import com.paragon.api.setting.Setting;
import me.wolfsurge.cerauno.listener.Listener;
import net.minecraft.block.Block;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * somewhat bad autocrystal. I have looked at some other client's ACs whilst writing this, but it isn't really skidded.
 * @author Surge
 */
@SideOnly(Side.CLIENT)
public class AutoCrystal extends Module {

    public static AutoCrystal INSTANCE;

    // Order of operations
    public static Setting<Order> order = new Setting<>("Order", Order.PLACE_EXPLODE)
            .setDescription("The order of operations to perform");

    public static Setting<Timing> timing = new Setting<>("Timing", Timing.LINEAR)
            .setDescription("When to perform actions");

    public static Setting<Heuristic> heuristic = new Setting<>("Heuristic", Heuristic.MINIMAX)
            .setDescription("The algorithm for calculating damage");


    // Targeting settings
    public static Setting<Boolean> targeting = new Setting<>("Targeting", true)
            .setDescription("Settings for targeting players\"");

    public static Setting<TargetPriority> targetPriority = new Setting<>("Priority", TargetPriority.DISTANCE)
            .setDescription("The way to sort possible targets")
            .setParentSetting(targeting);

    public static Setting<Boolean> targetFriends = new Setting<>("Friends", false)
            .setDescription("Target friends")
            .setParentSetting(targeting);

    public static Setting<Float> targetRange = new Setting<>("Range", 10f, 1f, 15f, 0.1f)
            .setDescription("The range to target players")
            .setParentSetting(targeting);

    // Place settings
    public static Setting<Boolean> place = new Setting<>("Place", true)
            .setDescription("Automatically place crystals");

    public static Setting<When> placeWhen = new Setting<>("When", When.SILENT_SWITCH)
            .setDescription("When to place crystals")
            .setParentSetting(place);

    public static Setting<Float> placeRange = new Setting<>("Range", 5f, 1f, 7f, 0.1f)
            .setDescription("The range to place")
            .setParentSetting(place);

    public static Setting<Boolean> placeMax = new Setting<>("Limit", true)
            .setDescription("Limit the amount of times we can attempt to place on a position")
            .setParentSetting(place);

    public static Setting<Double> placeMaxAmount = new Setting<>("Amount", 3D, 1D, 10D, 1D)
            .setDescription("The amount of times we can attempt to place on a position")
            .setParentSetting(place)
            .setVisibility(placeMax::getValue);

    public static Setting<Double> placeDelay = new Setting<>("Delay", 10D, 0D, 500D, 1D)
            .setDescription("The delay between placing crystals")
            .setParentSetting(place);

    public static Setting<Rotate> placeRotate = new Setting<>("Rotate", Rotate.PACKET)
            .setDescription("Rotate to the position you are placing at")
            .setParentSetting(place);

    public static Setting<Double> placeYOffset = new Setting<>("Offset", 0.75D, 0D, 1.25D, 0.01D)
            .setDescription("The Y offset when rotating")
            .setParentSetting(place)
            .setVisibility(() -> !placeRotate.getValue().equals(Rotate.NONE));

    public static Setting<Boolean> placeRotateBack = new Setting<>("RotateBack", true)
            .setDescription("Rotate back to your original rotation")
            .setParentSetting(place)
            .setVisibility(() -> !placeRotate.getValue().equals(Rotate.NONE));

    public static Setting<Boolean> placeRaytrace = new Setting<>("Raytrace", true)
            .setDescription("Checks if you can raytrace to the position")
            .setParentSetting(place);

    public static Setting<Boolean> multiplace = new Setting<>("Multiplace", false)
            .setDescription("Place multiple crystals")
            .setParentSetting(place);

    public static Setting<Float> placeMinDamage = new Setting<>("MinDamage", 4f, 0f, 36f, 1f)
            .setDescription("The minimum amount of damage to do to the target")
            .setParentSetting(place);

    public static Setting<Float> placeMaxLocal = new Setting<>("MaxLocal", 8f, 0f, 36f, 1f)
            .setDescription("The minimum amount of damage to inflict upon yourself")
            .setParentSetting(place);

    public static Setting<Boolean> placePacket = new Setting<>("Packet", false)
            .setDescription("Place with only a packet")
            .setParentSetting(place);

    public static Setting<Swing> placeSwing = new Setting<>("Swing", Swing.MAIN_HAND)
            .setDescription("Swing when placing a crystal")
            .setParentSetting(place);


    // Explode settings
    public static Setting<Boolean> explode = new Setting<>("Explode", true)
            .setDescription("Automatically explode crystals");

    public static Setting<Float> explodeRange = new Setting<>("Range", 5f, 1f, 7f, 0.1f)
            .setDescription("The range to explode crystals")
            .setParentSetting(explode);

    public static Setting<Double> explodeDelay = new Setting<>("Delay", 10D, 0D, 500D, 1D)
            .setDescription("The delay between exploding crystals")
            .setParentSetting(explode);

    public static Setting<ExplodeFilter> explodeFilter = new Setting<>("Filter", ExplodeFilter.SMART)
            .setDescription("What crystals to explode")
            .setParentSetting(explode);

    public static Setting<Boolean> explodeMax = new Setting<>("Limit", true)
            .setDescription("Limit the amount of attacks on a crystal")
            .setParentSetting(explode);

    public static Setting<Float> explodeLimitMax = new Setting<>("LimitValue", 5f, 1f, 10f, 1f)
            .setDescription("When to start ignoring the crystals")
            .setParentSetting(explode)
            .setVisibility(explodeMax::getValue);

    public static Setting<Double> explodeTicksExisted = new Setting<>("TicksExisted", 0D, 0D, 5D, 1D)
            .setDescription("Check the amount of ticks the crystal has existed before exploding")
            .setParentSetting(explode);

    public static Setting<Boolean> explodeRaytrace = new Setting<>("Raytrace", false)
            .setDescription("Checks that you can raytrace to the crystal")
            .setParentSetting(explode);

    public static Setting<Rotate> explodeRotate = new Setting<>("Rotate", Rotate.PACKET)
            .setDescription("How to rotate to the crystal")
            .setParentSetting(explode);

    public static Setting<Double> explodeYOffset = new Setting<>("Offset", 0.25D, 0D, 1.5D, 0.01D)
            .setDescription("The Y offset to rotate to the crystal")
            .setParentSetting(explode)
            .setVisibility(() -> !explodeRotate.getValue().equals(Rotate.NONE));

    public static Setting<Boolean> explodeRotateBack = new Setting<>("RotateBack", true)
            .setDescription("Rotate back to your original rotation")
            .setParentSetting(explode)
            .setVisibility(() -> !explodeRotate.getValue().equals(Rotate.NONE));

    public static Setting<AntiWeakness> antiWeakness = new Setting<>("AntiWeakness", AntiWeakness.SWITCH)
            .setDescription("If you have the weakness effect, you will still be able to explode crystals")
            .setParentSetting(explode);

    public static Setting<Boolean> strictInventory = new Setting<>("StrictInventory", true)
            .setDescription("Fake opening your inventory when you switch")
            .setParentSetting(explode)
            .setVisibility(() -> !antiWeakness.getValue().equals(AntiWeakness.OFF));

    public static Setting<Boolean> packetExplode = new Setting<>("Packet", false)
            .setDescription("Explode crystals with a packet only")
            .setParentSetting(explode);

    public static Setting<Swing> explodeSwing = new Setting<>("Swing", Swing.BOTH)
            .setDescription("How to swing your hand")
            .setParentSetting(explode);

    public static Setting<Float> explodeMinDamage = new Setting<>("MinDamage", 4f, 0f, 36f, 1f)
            .setDescription("The minimum amount of damage to do to the target")
            .setParentSetting(explode)
            .setVisibility(() -> explodeFilter.getValue().equals(ExplodeFilter.SMART) || explodeFilter.getValue().equals(ExplodeFilter.SELF_SMART));

    public static Setting<Float> explodeMaxLocal = new Setting<>("MaxLocal", 8f, 0f, 36f, 1f)
            .setParentSetting(explode)
            .setVisibility(() -> explodeFilter.getValue().equals(ExplodeFilter.SMART) || explodeFilter.getValue().equals(ExplodeFilter.SELF_SMART));

    public static Setting<SetDead> explodeSync = new Setting<>("Sync", SetDead.SOUND)
            .setDescription("Sync crystal explosions")
            .setParentSetting(explode);


    // Override settings
    public static Setting<Boolean> override = new Setting<>("Override", true)
            .setDescription("Override minimum damage when certain things happen");

    public static Setting<Boolean> overrideHealth = new Setting<>("Health", true)
            .setDescription("Override if the target's health is below a value")
            .setParentSetting(override);

    public static Setting<Float> overrideHealthValue = new Setting<>("OverrideHealth", 10f, 0f, 36f, 1f)
            .setDescription("If the targets health is this value or below, ignore minimum damage")
            .setParentSetting(override)
            .setVisibility(override::getValue);

    public static Setting<Boolean> overrideTotalArmour = new Setting<>("Armour", true)
            .setDescription("Override if the target's total armour durability is below a certain value")
            .setParentSetting(override);

    public static Setting<Float> overrideTotalArmourValue = new Setting<>("ArmourValue", 10f, 0f, 100f, 1f)
            .setDescription("The value which we will start to override at (in %)")
            .setParentSetting(override);

    public static Setting<Bind> forceOverride = new Setting<>("ForceOverride", new Bind(0, Bind.Device.KEYBOARD))
            .setDescription("Force override when you press a key")
            .setParentSetting(override);

    public static Setting<Boolean> ignoreMax = new Setting<>("IgnoreMax", true)
            .setDescription("Do not ignore the limits if we are overriding")
            .setParentSetting(override)
            .setVisibility(explodeMax::getValue);

    public static Setting<Boolean> ignoreMaxLocal = new Setting<>("IgnoreMaxLocal", true)
            .setDescription("Place or explode even if the damage done to us is larger than the max local damage")
            .setParentSetting(override);


    // Pause settings
    public static Setting<Boolean> pause = new Setting<>("Pause", true)
            .setDescription("Pause if certain things are happening");

    public static Setting<Boolean> pauseEating = new Setting<>("Eating", true)
            .setDescription("Pause when eating")
            .setParentSetting(pause);

    public static Setting<Boolean> pauseDrinking = new Setting<>("Drinking", true)
            .setDescription("Pause when drinking")
            .setParentSetting(pause);

    public static Setting<Boolean> pauseHealth = new Setting<>("Health", true)
            .setDescription("Pause when your health is below a specified value")
            .setParentSetting(pause);

    public static Setting<Float> pauseHealthValue = new Setting<>("HealthValue", 10f, 1f, 20f, 1f)
            .setParentSetting(pause)
            .setVisibility(pauseHealth::getValue);

    public static Setting<Boolean> antiSuicide = new Setting<>("AntiSuicide", true)
            .setDescription("Does not explode / place the crystal if it will pop or kill you")
            .setParentSetting(pause);

    public static Setting<Boolean> randomPause = new Setting<>("RandomPause", false)
            .setDescription("Randomly pauses to try and prevent you from being kicked")
            .setParentSetting(pause);

    public static Setting<Float> randomChance = new Setting<>("Chance", 50f, 2f, 100f, 1f)
            .setDescription("The chance to pause")
            .setParentSetting(pause)
            .setVisibility(randomPause::getValue);

    public static Setting<Boolean> confirmRotate = new Setting<>("ConfirmRotate", true)
            .setDescription("Make sure you have rotated before preceding with the action");

    public static Setting<Float> confirmThreshold = new Setting<>("ConfirmThreshold", 50f, 0f, 100f, 1f)
            .setDescription("The threshold to determine whether we are looking away from the position")
            .setVisibility(confirmRotate::getValue);

    // Render settings
    public static Setting<Boolean> render = new Setting<>("Render", true)
            .setDescription("Render the placement");

    public static Setting<Render> renderMode = new Setting<>("Mode", Render.BOTH)
            .setDescription("How to render placement")
            .setParentSetting(render);

    public static Setting<Float> renderOutlineWidth = new Setting<>("OutlineWidth", 0.5f, 0.1f, 2f, 0.1f)
            .setDescription("The width of the lines")
            .setParentSetting(render);

    public static Setting<Color> renderColour = new Setting<>("FillColour", new Color(185, 19, 255, 130))
            .setDescription( "The colour of the fill")
            .setParentSetting(render);

    public static Setting<Color> renderOutlineColour = new Setting<>("OutlineColour", new Color(185, 19, 255))
            .setParentSetting(render);

    public static Setting<Boolean> renderDamageNametag = new Setting<>("DamageNametag", true)
            .setDescription("Render the damage nametag")
            .setParentSetting(render);

    // The current player we are targeting
    private EntityPlayer currentTarget;

    // The current crystal we are targeting
    private Crystal currentCrystal;

    // The current position we are placing at
    private CrystalPosition currentPlacement;

    // Exists purely to stop the flickering in the HUD info
    private CrystalPosition backlogPlacement;

    // Timers
    private final Timer explodeTimer = new Timer();
    private final Timer placeTimer = new Timer();

    // List of crystals we have placed
    private final List<BlockPos> selfPlacedCrystals = new ArrayList<>();

    // The current action we are performing
    private ActionState currentActionState = ActionState.PLACING;

    // Map of crystals we have attacked. Key is ID, Value is the amount of times we have attacked it
    private final Map<Integer, Integer> explodeLimitMap = new HashMap<>();

    // Map of block positions we have attempted to place on
    private final Map<BlockPos, Integer> placeLimitMap = new HashMap<>();

    public AutoCrystal() {
        super("AutoCrystal", Category.COMBAT, "Automatically places and explodes crystals");

        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        reset();
    }

    @Listener
    public void onSettingUpdate(SettingUpdateEvent event) {
        if (event.getSetting() == explodeFilter || event.getSetting() == explodeRotate || event.getSetting() == placeRotate) {
            reset();
        }
    }

    @Override
    public void onTick() {
        if (nullCheck()) {
            return;
        }

        // Pause if we are supposed to
        if (pause.getValue() && (pauseHealth.getValue() && EntityUtil.getEntityHealth(mc.player) <= pauseHealthValue.getValue() || pauseEating.getValue() && PlayerUtil.isPlayerEating() || pauseDrinking.getValue() && PlayerUtil.isPlayerDrinking() || randomPause.getValue() && new Random().nextInt(randomChance.getValue().intValue()) == 1)) {
            return;
        }

        // Set our target
        currentTarget = getCurrentTarget();

        // Don't do anything if we don't have a target
        if (currentTarget == null) {
            reset();
            return;
        }

        // Get overriding state
        // Called once because otherwise we do the same logic several times
        boolean overriding = isOverriding(currentTarget);

        // Add target to AutoEZ list
        AutoEZ.addTarget(currentTarget.getName());

        switch (order.getValue()) {
            case PLACE_EXPLODE:
                if (!timing.getValue().equals(Timing.SEQUENTIAL) || currentActionState.equals(ActionState.PLACING)) {
                    // Find placement
                    currentPlacement = findBestPosition(overriding);

                    if (currentPlacement != null) {
                        placeSearchedPosition();
                    }
                }

                if (!timing.getValue().equals(Timing.SEQUENTIAL) || currentActionState.equals(ActionState.EXPLODING)) {
                    // Find crystal
                    currentCrystal = findBestCrystal(overriding);

                    if (currentCrystal != null) {
                        explodeSearchedCrystal();
                    }
                }

                break;

            case EXPLODE_PLACE:
                if (!timing.getValue().equals(Timing.SEQUENTIAL) || currentActionState.equals(ActionState.EXPLODING)) {
                    // Find crystal
                    currentCrystal = findBestCrystal(overriding);

                    if (currentCrystal != null) {
                        explodeSearchedCrystal();
                    }
                }

                if (!timing.getValue().equals(Timing.SEQUENTIAL) || currentActionState.equals(ActionState.PLACING)) {
                    // Find placement
                    currentPlacement = findBestPosition(overriding);

                    if (currentPlacement != null) {
                        placeSearchedPosition();
                    }
                }

                break;

        }

        currentActionState = currentActionState.equals(ActionState.PLACING) ? ActionState.EXPLODING : ActionState.PLACING;
    }

    @Override
    public void onRender3D() {
        // Render highlight
        if (render.getValue() && currentPlacement != null && place.getValue()) {
            // Render fill
            if (renderMode.getValue().equals(Render.FILL) || renderMode.getValue().equals(Render.BOTH)) {
                RenderUtil.drawFilledBox(BlockUtil.getBlockBox(currentPlacement.getPosition()), ColourUtil.integrateAlpha(renderColour.getValue(), renderColour.getAlpha()));
            }

            // Render outline
            if (renderMode.getValue().equals(Render.OUTLINE) || renderMode.getValue().equals(Render.BOTH)) {
                RenderUtil.drawBoundingBox(BlockUtil.getBlockBox(currentPlacement.getPosition()), renderOutlineWidth.getValue(), renderOutlineColour.getValue());
            }

            // Render damage nametag
            if (renderDamageNametag.getValue()) {
                RenderUtil.drawNametagText("[" + (int) currentPlacement.getTargetDamage() + ", " + (int) currentPlacement.getSelfDamage() + "]", new Vec3d(currentPlacement.getPosition().getX() + 0.5, currentPlacement.getPosition().getY() + 0.5, currentPlacement.getPosition().getZ() + 0.5), -1);
            }
        }
    }

    @Listener
    public void onPacketReceive(PacketEvent.PreReceive event) {
        // If we are trying to use and item on a block
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            // Get packet
            CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket();

            // Check we are holding end crystals
            if (mc.player.getHeldItem(packet.getHand()).getItem().equals(Items.END_CRYSTAL)) {
                // If we can place a crystal on that block, add it to our self placed crystals list
                selfPlacedCrystals.add(packet.getPos());
            }
        }

        // Check it's a sound packet
        if (event.getPacket() instanceof SPacketSoundEffect && explodeSync.getValue().equals(SetDead.SOUND)) {
            // Get packet
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();

            // Check it's an explosion sound
            if (packet.getSound().equals(SoundEvents.ENTITY_GENERIC_EXPLODE) && packet.getCategory().equals(SoundCategory.BLOCKS)) {
                // Iterate through loaded entities
                for (Entity entity : mc.world.loadedEntityList) {
                    // If the entity isn't an ender crystal, or it is dead, ignore
                    if (!(entity instanceof EntityEnderCrystal) || entity.isDead) {
                        continue;
                    }

                    // If the crystal is close to the explosion sound origin, set the crystals state to dead
                    if (entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6) {
                        entity.setDead();
                    }
                }
            }
        }
    }

    /**
     * Gets the best player to target
     * @return The best player to target
     */
    public EntityPlayer getCurrentTarget() {
        // All valid targets
        List<EntityPlayer> validTargets = new ArrayList<>();

        // Iterate through loaded entities
        for (Entity entity : mc.world.loadedEntityList) {
            // Check it's a player that isn't us
            if (entity instanceof EntityOtherPlayerMP) {
                // Get player
                EntityPlayer entityPlayer = (EntityPlayer) entity;

                // Make sure the player is a valid target
                if (entityPlayer.isDead || entityPlayer.getHealth() <= 0 || EntityUtil.isTooFarAwayFromSelf(entityPlayer, targetRange.getValue())) {
                    continue;
                }

                // If it's a friend, and we don't want to target friends, ignore
                if (!targetFriends.getValue()) {
                    if (Paragon.INSTANCE.getSocialManager().isFriend(entityPlayer.getName())) {
                        continue;
                    }
                }

                // Add to valid targets list
                validTargets.add(entityPlayer);
            }
        }

        // Return null if there are no valid targets
        if (validTargets.isEmpty()) {
            return null;
        }

        // Sort by priority
        switch (targetPriority.getValue()) {
            // Sort by distance
            case DISTANCE:
                validTargets.sort(Comparator.comparingDouble(target -> mc.player.getDistance(target)));
                break;

            // Sort by health
            case HEALTH:
                validTargets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;

            // Sort by total armour value
            case ARMOUR:
                validTargets.sort(Comparator.comparingDouble(target -> {
                    float totalArmour = 0;

                    // Iterate through target's armour slots
                    for (ItemStack armour : target.getArmorInventoryList()) {
                        // Don't do anything if they don't have an item in the slot
                        if (armour.isEmpty()) {
                            continue;
                        }

                        // Add item damage to total
                        totalArmour += armour.getItemDamage();
                    }

                    return totalArmour;
                }));

                break;
        }

        return validTargets.get(0);
    }

    /**
     * Explodes the searched crystal
     */
    public void explodeSearchedCrystal() {
        // Check we want to explode
        if (explode.getValue()) {
            // Check we want to explode a crystal
            if (!explodeTimer.hasMSPassed(explodeDelay.getValue()) || currentCrystal.getSelfDamage() > EntityUtil.getEntityHealth(mc.player) && antiSuicide.getValue()) {
                return;
            }

            // Get our current slot so we can switch back
            int antiWeaknessSlot = mc.player.inventory.currentItem;

            // Check we want to apply anti weakness
            if (!antiWeakness.getValue().equals(AntiWeakness.OFF) && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                // If we want to fake opening our inventory, send the opening inventory packet
                if (strictInventory.getValue()) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.OPEN_INVENTORY));
                }

                // Get the best sword
                int hotbarSwordSlot = InventoryUtil.getItemInHotbar(Items.DIAMOND_SWORD);

                // If we have found a sword, switch to it
                if (hotbarSwordSlot != -1) {
                    InventoryUtil.switchToSlot(hotbarSwordSlot, antiWeakness.getValue().equals(AntiWeakness.SILENT));
                }
            }

            // Get our original rotation before rotating to the crystal
            Vec2f originalPlayerRotation = new Vec2f(mc.player.rotationYaw, mc.player.rotationPitch);

            // Get rotation
            Vec2f rotationVec = RotationUtil.getRotationToVec3d(new Vec3d(currentCrystal.getCrystal().posX, currentCrystal.getCrystal().posY + explodeYOffset.getValue(), currentCrystal.getCrystal().posZ));

            // Check we want to rotate
            if (!explodeRotate.getValue().equals(Rotate.NONE)) {
                Rotation rotation = new Rotation(rotationVec.x, rotationVec.y, explodeRotate.getValue(), RotationPriority.HIGHEST);

                // Send rotation
                Paragon.INSTANCE.getRotationManager().addRotation(rotation);
            }

            if (confirmRotate.getValue() && !MathsUtil.isNearlyEqual(mc.player.rotationYaw, rotationVec.x, confirmThreshold.getValue()) && !MathsUtil.isNearlyEqual(mc.player.rotationPitch, rotationVec.y, confirmThreshold.getValue()) && placeRotate.getValue().equals(Rotate.LEGIT)) {
                return;
            }

            if (packetExplode.getValue()) {
                // Explode with a packet
                mc.player.connection.sendPacket(new CPacketUseEntity(currentCrystal.getCrystal()));
            } else {
                // Attack crystal
                mc.playerController.attackEntity(mc.player, currentCrystal.getCrystal());
            }

            // If we want to set the crystal to dead as soon as we attack, do that
            if (explodeSync.getValue().equals(SetDead.ATTACK)) {
                currentCrystal.getCrystal().setDead();
            }

            // Remove it from our self placed crystals
            selfPlacedCrystals.remove(currentCrystal.getCrystal().getPosition().down());

            // Swing our arm
            swing(explodeSwing.getValue());

            // Rotate back to our original rotation
            if (!explodeRotate.getValue().equals(Rotate.NONE) && explodeRotateBack.getValue()) {
                Rotation rotation = new Rotation(originalPlayerRotation.x, originalPlayerRotation.y, explodeRotate.getValue(), RotationPriority.HIGHEST);

                // Send rotation
                Paragon.INSTANCE.getRotationManager().addRotation(rotation);
            }

            // Check we want to switch
            if (!antiWeakness.getValue().equals(AntiWeakness.OFF) && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                // Fake opening inventory
                if (strictInventory.getValue()) {
                    mc.player.connection.sendPacket(new CPacketCloseWindow(mc.player.inventoryContainer.windowId));
                }

                // Switch to slot
                if (antiWeaknessSlot != -1) {
                    InventoryUtil.switchToSlot(antiWeaknessSlot, antiWeakness.getValue().equals(AntiWeakness.SILENT));
                }
            }

            explodeTimer.reset();
        }
    }

    /**
     * Places a crystal at the searched position
     */
    public void placeSearchedPosition() {
        // Check we want to place a crystal
        if (!placeTimer.hasMSPassed(placeDelay.getValue()) && currentPlacement.getSelfDamage() > EntityUtil.getEntityHealth(mc.player) && antiSuicide.getValue()) {
            return;
        }

        boolean hasSwitched = false;
        int oldSlot = mc.player.inventory.currentItem;

        switch (placeWhen.getValue()) {
            case HOLDING:
                hasSwitched = InventoryUtil.isHolding(Items.END_CRYSTAL);
                break;

            case SWITCH:
            case SILENT_SWITCH:
                int silentCrystalSlot = InventoryUtil.getItemInHotbar(Items.END_CRYSTAL);

                if (silentCrystalSlot == -1) {
                    break;
                } else {
                    InventoryUtil.switchToSlot(silentCrystalSlot, false);
                    hasSwitched = true;
                }

                break;
        }

        // We haven't switched, don't place
        if (!hasSwitched) {
            return;
        }

        // Get our current rotation
        Vec2f originalRotation = new Vec2f(mc.player.rotationYaw, mc.player.rotationPitch);

        // Get rotation
        Vec2f placeRotation = RotationUtil.getRotationToBlockPos(currentPlacement.getPosition(), placeYOffset.getValue());

        // Check we want to rotate
        if (!placeRotate.getValue().equals(Rotate.NONE)) {
            Rotation rotation = new Rotation(placeRotation.x, placeRotation.y, placeRotate.getValue(), RotationPriority.HIGHEST);
            Paragon.INSTANCE.getRotationManager().addRotation(rotation);
        }

        // Confirm rotations for strict
        if (confirmRotate.getValue() && (!MathsUtil.isNearlyEqual(mc.player.rotationYaw, placeRotation.x, confirmThreshold.getValue()) || !MathsUtil.isNearlyEqual(mc.player.rotationPitch, placeRotation.y, confirmThreshold.getValue())) && placeRotate.getValue().equals(Rotate.LEGIT)) {
            return;
        }

        EnumHand placeHand = InventoryUtil.getHandHolding(Items.END_CRYSTAL);

        // Let's call this, it fixes the packet place bug, and it shouldn't do anything bad afaik.
        ((IPlayerControllerMP) mc.playerController).hookSyncCurrentPlayItem();

        if (placePacket.getValue() && placeHand != null) {
            // Send packet
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(currentPlacement.getPosition(), currentPlacement.getFacing(), mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : placeHand, (float) currentPlacement.facingVec.x, (float) currentPlacement.facingVec.y, (float) currentPlacement.facingVec.z));

            // Swing arm
            swing(placeSwing.getValue());
        } else if (placeHand != null) {
            // Place crystal
            if (mc.playerController.processRightClickBlock(mc.player, mc.world, currentPlacement.getPosition(), currentPlacement.getFacing(), new Vec3d(currentPlacement.getFacing().getDirectionVec()), mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : placeHand).equals(EnumActionResult.SUCCESS)) {
                // Swing arm
                swing(placeSwing.getValue());
            }
        }

        // Add position to our self placed crystals
        selfPlacedCrystals.add(currentPlacement.getPosition());

        // Check we want to rotate back
        if (!placeRotate.getValue().equals(Rotate.NONE) && placeRotateBack.getValue()) {
            // Rotate back
            Rotation rotation = new Rotation(originalRotation.x, originalRotation.y, placeRotate.getValue(), RotationPriority.HIGHEST);
            Paragon.INSTANCE.getRotationManager().addRotation(rotation);
        }

        if (placeWhen.getValue().equals(When.SILENT_SWITCH)) {
            InventoryUtil.switchToSlot(oldSlot, false);
        }

        placeTimer.reset();
    }

    /**
     * Finds the best crystal to attack
     * @return The best crystal to attack
     */
    public Crystal findBestCrystal(boolean overriding) {
        // The best crystal (we will return this)
        Crystal crystal = null;

        // Check we want to explode
        if (explode.getValue()) {
            // Iterate through loaded entities
            for (Entity entity : mc.world.loadedEntityList) {
                // Check the entity is a crystal
                if (entity instanceof EntityEnderCrystal && !entity.isDead) {
                    // Check the crystal is valid
                    if (entity.ticksExisted < explodeTicksExisted.getValue().intValue() || EntityUtil.isTooFarAwayFromSelf(entity, explodeRange.getValue()) || explodeRaytrace.getValue() && !mc.player.canEntityBeSeen(entity)) {
                        continue;
                    }

                    // We have already tried to explode this crystal
                    if (explodeMax.getValue() && explodeLimitMap.containsKey(entity.getEntityId()) && explodeLimitMap.get(entity.getEntityId()).floatValue() > explodeLimitMax.getValue().intValue()) {
                        if (!overriding && !ignoreMax.getValue()) {
                            continue;
                        }
                    } else {
                        explodeLimitMap.put(entity.getEntityId(), explodeLimitMap.getOrDefault(entity.getEntityId(), 0) + 1);
                    }

                    // Get the crystals position as a vector
                    Vec3d vec = new Vec3d(entity.posX, entity.posY, entity.posZ);

                    // Crystal
                    Crystal calculatedCrystal = new Crystal((EntityEnderCrystal) entity, calculateDamage(vec, currentTarget), calculateDamage(vec, mc.player));

                    // Position of crystal
                    CrystalPosition crystalPos = new CrystalPosition(calculatedCrystal.getCrystal().getPosition(), null, new Vec3d(0, 0, 0), calculatedCrystal.getTargetDamage(), calculatedCrystal.getSelfDamage());

                    // Check it meets our filter
                    switch (explodeFilter.getValue()) {
                        case SELF:
                            // Check it's in our self placed crystals
                            if (!selfPlacedCrystals.contains(crystalPos.getPosition())) {
                                continue;
                            }

                            break;

                        case SELF_SMART:
                            // Check it's in our self placed crystals
                            if (!selfPlacedCrystals.contains(crystalPos.getPosition())) {
                                continue;
                            }

                            // Check it meets our max local requirement
                            if (calculatedCrystal.getSelfDamage() > explodeMaxLocal.getValue()) {
                                if (!(overriding && ignoreMaxLocal.getValue())) {
                                    continue;
                                }
                            }

                            // Check it meets our minimum damage requirement
                            if (calculatedCrystal.getTargetDamage() < explodeMinDamage.getValue() && !overriding) {
                                continue;
                            }

                            break;

                        case SMART:
                            // Check it meets our max local requirement
                            if (calculatedCrystal.getSelfDamage() > explodeMaxLocal.getValue()) {
                                if (!(overriding && ignoreMaxLocal.getValue())) {
                                    continue;
                                }
                            }

                            // Check it meets our minimum damage requirement
                            if (calculatedCrystal.getTargetDamage() < explodeMinDamage.getValue() && !overriding) {
                                continue;
                            }

                            break;
                    }

                    // Set the crystal to this if: the current best crystal is null, or this crystal's target damage is higher than the last crystal checked
                    if (crystal == null || calculateHeuristic(calculatedCrystal.getSelfDamage(), calculatedCrystal.getTargetDamage(), mc.player.getDistance(entity), heuristic.getValue()) > calculateHeuristic(crystal.getSelfDamage(), crystal.getTargetDamage(), mc.player.getDistance(entity), heuristic.getValue())) {
                        crystal = calculatedCrystal;
                    }
                }
            }
        }

        return crystal;
    }

    /**
     * Finds the best position to place at
     * @return The best position to place at
     */
    public CrystalPosition findBestPosition(boolean overriding) {
        List<CrystalPosition> crystalPositions = new ArrayList<>();

        // Check we want to place
        if (place.getValue()) {
            // Iterate through blocks around us
            for (BlockPos pos : BlockUtil.getSphere(placeRange.getValue(), true)) {
                // Check we can place crystals on this block
                if (!canPlaceCrystal(pos)) {
                    continue;
                }

                if (placeMax.getValue() && placeLimitMap.containsKey(pos) && placeLimitMap.get(pos) >= placeMaxAmount.getValue()) {
                    if (!overriding && !ignoreMax.getValue()) {
                        continue;
                    }
                } else {
                    placeLimitMap.put(pos, placeLimitMap.getOrDefault(pos, 0) + 1);
                }

                // Position we are placing at
                Vec3d placeVec = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

                // Position we will calculate damage at
                Vec3d damageVec = new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);

                // Get the direction we want to face
                EnumFacing facing = EnumFacing.getDirectionFromEntityLiving(pos, mc.player);
                Vec3d facingVec = null;
                RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(mc.player.getPositionEyes(1), mc.player.getPositionEyes(1).add(new Vec3d(placeVec.x * placeRange.getValue(), placeVec.y * placeRange.getValue(), placeVec.z * placeRange.getValue())), false, false, true);
                RayTraceResult middleResult = mc.world.rayTraceBlocks(mc.player.getPositionEyes(1), new Vec3d(pos).add(new Vec3d(0.5, 0.5, 0.5)));

                // Check we hit a block
                if (middleResult != null && middleResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
                    facing = middleResult.sideHit;

                    // We can place if we are at max height by doing this
                    if (pos.getY() >= (mc.world.getActualHeight() - 1)) {
                        facing = EnumFacing.DOWN;
                    }
                }

                // Get angles
                if (rayTraceResult != null && rayTraceResult.hitVec != null) {
                    facingVec = new Vec3d(rayTraceResult.hitVec.x - pos.getX(), rayTraceResult.hitVec.y - pos.getY(), rayTraceResult.hitVec.z - pos.getZ());
                }

                // Create new crystal position
                CrystalPosition crystalPosition = new CrystalPosition(pos, facing, facingVec, calculateDamage(damageVec, currentTarget), calculateDamage(damageVec, mc.player));

                // Check it's below or equal to our maximum local damage requirement
                if (crystalPosition.getSelfDamage() > placeMaxLocal.getValue() && (!(overriding && ignoreMaxLocal.getValue()))) {
                    continue;
                }

                // Check it's above or equal to our minimum damage requirement
                if (!overriding && crystalPosition.getTargetDamage() < placeMinDamage.getValue()) {
                    continue;
                }

                crystalPositions.add(crystalPosition);
            }
        }

        crystalPositions.sort(Comparator.comparingDouble(position -> calculateHeuristic(position, heuristic.getValue())));
        Collections.reverse(crystalPositions);

        if (!crystalPositions.isEmpty()) {
            backlogPlacement = crystalPositions.get(0);
            return crystalPositions.get(0);
        }

        return null;
    }

    public boolean isOverriding(EntityPlayer target) {
        if (override.getValue()) {
            if (overrideHealth.getValue() && EntityUtil.getEntityHealth(target) <= overrideHealthValue.getValue() || forceOverride.getValue().isPressed()) {
                return true;
            }

            if (overrideTotalArmour.getValue()) {
                // *Looked* at Cosmos for this, so thanks, just wanted to make sure I was doing this right :')

                float lowest = 100;

                // Iterate through target's armour
                for (ItemStack armourPiece : target.getArmorInventoryList()) {
                    // If it is an actual piece of armour
                    if (armourPiece != null && armourPiece.getItem() != Items.AIR) {
                        // Get durability
                        float durability = (armourPiece.getMaxDamage() - armourPiece.getItemDamage()) / (float) armourPiece.getMaxDamage() * 100;

                        // If it is less than the last lowest, set the lowest to this durability
                        if (durability < lowest) {
                            lowest = durability;
                        }
                    }
                }

                // We are overriding if the lowest durability is less or equal to the total armour value setting
                return lowest <= overrideTotalArmourValue.getValue();
            }
        }

        return false;
    }

    /**
     * Swings our hands
     * @param swing The hand to swing
     */
    public void swing(Swing swing) {
        switch (swing) {
            case MAIN_HAND:
                // Swing main hand
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            case OFFHAND:
                // Swing offhand
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case BOTH:
                // Swing both hands
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case PACKET:
                // Send a swing hand packet
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                break;
        }
    }

    /**
     * Calculates the heuristic
     * @param self The damage done to us
     * @param target The damage done to the target
     * @param distance The distance from the crystal
     * @param heuristic The heuristic type
     * @return The damage heuristic
     */
    public float calculateHeuristic(float self, float target, float distance, Heuristic heuristic) {
        switch (heuristic) {
            case DAMAGE:
                // Just target damage
                return target;
            case MINIMAX:
                // Target damage minus self damage
                return target - self;
            case UNIFORM:
                // Target damage minus self damage minus distance
                return target - self - distance;
        }

        return target;
    }

    /**
     * Calculates the heuristic based on a crystal position
     * @param crystal The crystal to calculate heuristic for
     * @param heuristic The heuristic type
     * @return The damage heuristic
     */
    public float calculateHeuristic(CrystalPosition crystal, Heuristic heuristic) {
        // Prevent NPE
        if (crystal == null) {
            return 0;
        }

        // Calculate and return heuristic
        return calculateHeuristic(crystal.getSelfDamage(), crystal.getTargetDamage(), (float) mc.player.getDistanceSq(crystal.getPosition()), heuristic);
    }

    /**
     * Checks if we can place a crystal on a block
     * @param pos The pos to check
     * @return Whether we can place a crystal on that block or not
     */
    public boolean canPlaceCrystal(BlockPos pos) {
        // Get block
        Block block = BlockUtil.getBlockAtPos(pos);

        // Check position is valid
        if (!(block.equals(Blocks.OBSIDIAN) || block.equals(Blocks.BEDROCK)) || !mc.world.isAirBlock(pos.up()) || !mc.world.getBlockState(pos.up(2)).getMaterial().isReplaceable() || mc.player.getPosition().equals(pos) || placeRaytrace.getValue() && !BlockUtil.canSeePos(pos)) {
            return false;
        }

        // Iterate through entities in the block above
        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up()))) {
            // If the entity is dead, or we aren't multiplacing, continue
            if (entity.isDead || !multiplace.getValue() && entity instanceof EntityEnderCrystal) {
                continue;
            }

            return false;
        }

        return true;
    }

    /**
     * Calculates the explosion damage based on a Vec3D
     * @param vec The vector to calculate damage from
     * @param entity The target
     * @return The damage done to the target
     */
    public float calculateDamage(Vec3d vec, EntityLivingBase entity) {
        float finalDamage = 0.0f;
        try {
            float doubleExplosionSize = 12.0F;
            double distancedSize = entity.getDistance(vec.x, vec.y, vec.z) / (double) doubleExplosionSize;
            double blockDensity = entity.world.getBlockDensity(new Vec3d(vec.x, vec.y, vec.z), entity.getEntityBoundingBox());
            double v = (1.0D - distancedSize) * blockDensity;
            float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));

            int diff = mc.world.getDifficulty().getDifficultyId();
            finalDamage = getBlastReduction(entity, damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f))), new Explosion(mc.world, null, vec.x, vec.y, vec.z, 6F, false, true));
        } catch (NullPointerException ignored) {
        }

        return finalDamage;
    }

    /**
     * Gets the blast reduction
     *
     * @param entity The entity to calculate damage for
     * @param damage The original damage
     * @param explosion The explosion
     * @return The blast reduction
     */
    public float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage *= 1.0F - f / 25.0F;

            if (entity.isPotionActive(MobEffects.WEAKNESS)) {
                damage = damage - (damage / 4);
            }

            damage = Math.max(damage, 0.0F);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    /**
     * Resets the process
     */
    public void reset() {
        this.currentTarget = null;
        this.currentCrystal = null;
        this.currentPlacement = null;
        this.backlogPlacement = null;
        this.selfPlacedCrystals.clear();
        this.explodeLimitMap.clear();
    }

    @Override
    public String getData() {
        return (currentTarget == null ? " No Target" : " " + currentTarget.getName() + " DMG " + (!isOverriding(currentTarget) ? "" : "[OVERRIDING] ") + (backlogPlacement == null ? "No Placement" : Math.round(calculateHeuristic(backlogPlacement, heuristic.getValue()))));
    }

    public enum Order {
        /**
         * Place then explode
         */
        PLACE_EXPLODE,

        /**
         * Explode then place
         */
        EXPLODE_PLACE
    }

    public enum Timing {
        /**
         * Run actions one after another
         */
        LINEAR,

        /**
         * Run actions on different ticks
         */
        SEQUENTIAL
    }

    public enum ActionState {
        /**
         * About to explode crystals
         */
        EXPLODING,

        /**
         * About to place crystals
         */
        PLACING
    }

    public enum Heuristic {
        /**
         * Just target damage
         */
        DAMAGE,

        /**
         * Target damage minus self damage
         */
        MINIMAX,

        /**
         * Target damage minus self damage minus distance
         */
        UNIFORM
    }

    public enum TargetPriority {
        /**
         * Target closest to us
         */
        DISTANCE,

        /**
         * Target with the lowest health
         */
        HEALTH,

        /**
         * Target with the lowest total armour value
         */
        ARMOUR
    }

    public enum When {
        /**
         * Only place when holding crystals
         */
        HOLDING,

        /**
         * Switch to crystals
         */
        SWITCH,

        /**
         * Silent switch to crystals (with a packet)
         */
        SILENT_SWITCH
    }

    public enum ExplodeFilter {
        /**
         * Explode all crystals regardless of parameters
         */
        ALL,

        /**
         * Explode crystals if they fit our minimum damage and maximum local damage requirements
         */
        SMART,

        /**
         * Explode crystals only if we have placed them
         */
        SELF,

        /**
         * Explode crystals if they fit our minimum damage and maximum local damage requirements, and only we have placed them
         */
        SELF_SMART
    }

    public enum AntiWeakness {
        /**
         * Switch to sword
         */
        SWITCH,

        /**
         * Silent switch to sword
         */
        SILENT,

        /**
         * Don't switch at all
         */
        OFF
    }

    public enum SetDead {
        /**
         * Set crystal's alive status to dead when we attack it
         */
        ATTACK,

        /**
         * Set crystal's alive status to dead when the explosion sound plays
         */
        SOUND,

        /**
         * Don't modify when we set it's alive status
         */
        OFF
    }

    public enum Swing {
        /**
         * Swing main hand
         */
        MAIN_HAND,

        /**
         * Swing offhand
         */
        OFFHAND,

        /**
         * Swing both hands
         */
        BOTH,

        /**
         * Send swing animation packet
         */
        PACKET,

        /**
         * Don't swing
         */
        OFF
    }

    public enum Render {
        /**
         * Render outline
         */
        OUTLINE,

        /**
         * Render fill
         */
        FILL,

        /**
         * Render both
         */
        BOTH
    }

    static class Crystal {
        // The entity
        private final EntityEnderCrystal crystal;

        // The damage we do to the target
        private final float targetDamage;

        // The damage we do to us
        private final float selfDamage;

        public Crystal(EntityEnderCrystal crystal, float targetDamage, float selfDamage) {
            this.crystal = crystal;
            this.targetDamage = targetDamage;
            this.selfDamage = selfDamage;
        }

        /**
         * Gets the crystal
         * @return The crystal
         */
        public EntityEnderCrystal getCrystal() {
            return crystal;
        }

        /**
         * Gets the target damage
         * @return The target damage
         */
        public float getTargetDamage() {
            return targetDamage;
        }

        /**
         * Gets the self damage
         * @return The self damage
         */
        public float getSelfDamage() {
            return selfDamage;
        }
    }

    static class CrystalPosition {
        // The position we will place at
        private final BlockPos position;

        // The direction we want to face
        private final EnumFacing facing;

        // Rotation angles
        private final Vec3d facingVec;

        // The damage we do to the target
        private final float targetDamage;

        // The damage we do to us
        private final float selfDamage;

        public CrystalPosition(BlockPos position, EnumFacing facing, Vec3d facingVec, float targetDamage, float selfDamage) {
            this.position = position;
            this.facing = facing;
            this.facingVec = facingVec;
            this.targetDamage = targetDamage;
            this.selfDamage = selfDamage;
        }

        /**
         * Gets the position
         * @return The position
         */
        public BlockPos getPosition() {
            return position;
        }

        /**
         * Gets the direction we want to face in
         * @return The direction we want to face in
         */
        public EnumFacing getFacing() {
            return facing;
        }

        /**
         * Gets the target damage
         * @return The target damage
         */
        public float getTargetDamage() {
            return targetDamage;
        }

        /**
         * Gets the self damage
         * @return The self damage
         */
        public float getSelfDamage() {
            return selfDamage;
        }
    }
}