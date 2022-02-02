package com.paragon.client.features.module.impl.render;

import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ColourSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.*;

public class Tracers extends Module {

    private final BooleanSetting passive = new BooleanSetting("Passives", "Draws lines to passive entities", true);
    private final ColourSetting passiveColour = new ColourSetting("Passive Colour", "The colour to render the passive tracers in", new Color(0, 255, 0, 180));

    private final BooleanSetting mobs = new BooleanSetting("Mobs", "Draws lines to monsters", true);
    private final ColourSetting mobColour = new ColourSetting("Mob Colour", "The colour to render the mob tracers in", new Color(255, 0, 0, 180));

    private final BooleanSetting players = new BooleanSetting("Players", "Draws lines to players", true);
    private final ColourSetting playerColour = new ColourSetting("Player Colour", "The colour to render the player tracers in", new Color(255, 255, 255, 180));

    private final BooleanSetting crystals = new BooleanSetting("Crystals", "Draws lines to ender crystals", true);
    private final ColourSetting crystalColour = new ColourSetting("Crystal Colour", "The colour to render the ender crystal tracers in", new Color(200, 0, 200, 180));

    private final NumberSetting lineWidth = new NumberSetting("Line Width", "How thick to render the lines", 0.5f, 0.1f, 2f, 0.1f);

    public Tracers() {
        super("Tracers", "Draws lines to entities in the world", Category.RENDER);
        this.addSettings(passive, passiveColour, mobs, mobColour, players, playerColour, crystals, crystalColour, lineWidth);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for(Entity entity : mc.world.loadedEntityList) {
            if(isEntityValid(entity))
                RenderUtil.drawTracer(entity, lineWidth.getValue(), getColourByEntity(entity));
        }
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
