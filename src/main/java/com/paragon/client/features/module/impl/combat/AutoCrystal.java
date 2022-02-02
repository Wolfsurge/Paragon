package com.paragon.client.features.module.impl.combat;

import com.paragon.api.event.events.UpdateEvent;
import com.paragon.api.util.player.InventoryUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.api.util.world.BlockUtil;
import com.paragon.api.util.world.EntityUtil;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private final NumberSetting targetRange = new NumberSetting("Target Range", "How far to find targets", 7, 1, 15, 1);
    private final ModeSetting targetPriority = new ModeSetting("Target Priority", "The mode to find targets", "Distance", new String[]{"Distance", "Health"});
    private final BooleanSetting reverseList = new BooleanSetting("Reverse Target List", "Reverse the targets list, to find the player with the highest value", false);

    // Placing
    private final BooleanSetting place = new BooleanSetting("Place", "Automatically place crystals", true);
    private final ModeSetting when = new ModeSetting("When", "When to place crystals", "Holding", new String[]{"Holding", "Switch", "Silent Switch"});
    private final NumberSetting placeRange = new NumberSetting("Place Range", "The limit on how far away you place crystals", 5, 1, 9, 1);

    // Breaking
    private final BooleanSetting breakCrystals = new BooleanSetting("Break", "Break crystals around you", true);

    // Other
    private final ModeSetting updateEvent = new ModeSetting("Update", "When to call the update", "Both", new String[]{"Client", "Server", "Both"});
    private final ModeSetting logic = new ModeSetting("Logic", "Whether to break or place crystals before the other", "Place -> Break", new String[]{"Break -> Place", "Place -> Break"});

    // HUD Info
    private final ModeSetting hudInfo = new ModeSetting("HUD Info", "What text to display on the Array List", "Target Info", new String[]{"Target Info", "Crystal Count"});
    private final BooleanSetting targetHealth = new BooleanSetting("Target Health", "Show target health in the Array List", true);

    // Variables
    private EntityPlayer currentTarget; // The current player being targeted

    public AutoCrystal() {
        super("AutoCrystal", "Automatically places and breaks crystals", Category.COMBAT);
        this.addSettings(targetRange, targetPriority, reverseList);
        this.addSettings(place, when, placeRange);
        this.addSettings(breakCrystals);
        this.addSettings(updateEvent, logic);
        this.addSettings(hudInfo, targetHealth);
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
        for(BlockPos pos : getPlaceableBlocks())
            RenderUtil.drawBoundingBox(BlockUtil.getBlockBox(pos), 1, Color.BLUE);
    }

    public void breakCrystals() {
        if(breakCrystals.isEnabled()) {

        }
    }

    public void placeCrystals() {
        if(place.isEnabled()) {
            if(!prePlace()) return;
        }
    }

    public ArrayList<BlockPos> getPlaceableBlocks() {
        ArrayList<BlockPos> availablePositions = new ArrayList<>();
        for(BlockPos pos : BlockUtil.getBlocksAroundPlayer(mc.player, placeRange.getValue())) {
            if(canPlaceCrystalOnBlock(pos))
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
                && BlockUtil.isIntercepted(pos) // Checks if there is an entity in the way
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
        if(!playerEntities.isEmpty())
            return (EntityPlayer) playerEntities.get(0);

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
     * Sets the target
     * @return Whether the target has been set
     */
    public boolean setTarget() {
        currentTarget = getBestTarget();

        if(currentTarget == null) return false;
        else return true;
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