package com.paragon.client.features.module.impl.render;

import com.paragon.api.util.render.RenderUtil;
import com.paragon.api.util.world.EntityUtil;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ColourSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class ESP extends Module {

    /* Entity settings */
    private final BooleanSetting passive = new BooleanSetting("Passives", "Highlight passive entities", true);
    private final ColourSetting passiveColour = new ColourSetting("Passive Colour", "The colour to highlight passive entities in", Color.GREEN);

    private final BooleanSetting mobs = new BooleanSetting("Mobs", "Highlight mobs", true);
    private final ColourSetting mobColour = new ColourSetting("Mob Colour", "The colour to highlight mobs in", Color.RED);

    private final BooleanSetting players = new BooleanSetting("Players", "Highlight player entities", true);
    private final ColourSetting playerColour = new ColourSetting("Player Colour", "The colour to highlight player entities in", new Color(255, 255, 255));

    private final BooleanSetting items = new BooleanSetting("Items", "Highlight items", true);

    private final BooleanSetting crystals = new BooleanSetting("Crystals", "Highlight end crystals", true);
    private final ColourSetting crystalColour = new ColourSetting("Crystal Colour", "The colour to highlight end crystals in", Color.GREEN);

    /* Mode and line width */
    private final ModeSetting mode = new ModeSetting("Mode", "How to render the entities", "Outline", new String[]{"Outline", "Glow", "Box"});
    private final NumberSetting lineWidth = new NumberSetting("Line Width", "How thick to render the outlines", 1, 0.1f, 2, 0.1f);

    public ESP() {
        super("ESP", "Highlights entities in the world", Category.RENDER);
        this.addSettings(passive, passiveColour, mobs, mobColour, players, playerColour, items, crystals, crystalColour, mode, lineWidth);
    }

    @Override
    public void onDisable() {
        for(Entity e : mc.world.loadedEntityList) {
            e.setGlowing(false);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            if(isEntityValid(e)) {
                espEntity(e);
            }
        }
    }

    /**
     * Highlights an entity
     * @param entityIn The entity to highlight
     */
    public void espEntity(Entity entityIn) {
        // Set it glowing if it's an item
        if(entityIn instanceof EntityItem) {
            entityIn.setGlowing(true);
            return;
        }

        if(mode.is("Outline"))
            mode.cycleMode(); // not implemented
        else if(mode.is("Box"))
            RenderUtil.drawBoundingBox(EntityUtil.getEntityBox(entityIn), lineWidth.getValue(), getColourByEntity(entityIn));
        else if(mode.is("Glow"))
            entityIn.setGlowing(true);

        if(!mode.is("Glow")) entityIn.setGlowing(false);
    }

    /**
     * Checks if an entity is valid
     * @param entityIn The entity to check
     * @return Is the entity valid
     */
    private boolean isEntityValid(Entity entityIn) {
        if(entityIn instanceof EntityLiving && !(entityIn instanceof EntityMob) && passive.isEnabled()) return true;
        else if(entityIn instanceof EntityMob && mobs.isEnabled()) return true;
        else if(entityIn instanceof EntityPlayer && entityIn != mc.player && players.isEnabled()) return true;
        else if(entityIn instanceof EntityItem && items.isEnabled()) return true;
        else return entityIn instanceof EntityEnderCrystal && crystals.isEnabled();
    }

    /**
     * Gets the entity's colour
     * @param entityIn The entity
     * @return The entity's colour
     */
    private Color getColourByEntity(Entity entityIn) {
        if(entityIn instanceof EntityLiving && !(entityIn instanceof EntityMob)) return passiveColour.getColour();
        else if(entityIn instanceof EntityMob) return mobColour.getColour();
        else if(entityIn instanceof EntityPlayer && entityIn != mc.player) return playerColour.getColour();
        else if(entityIn instanceof EntityEnderCrystal) return crystalColour.getColour();
        else return new Color(0);
    }
}
