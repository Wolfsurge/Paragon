package com.paragon.client.systems.module.impl.render;

import com.paragon.api.event.client.SettingUpdateEvent;
import com.paragon.api.event.render.ShaderColourEvent;
import com.paragon.api.event.render.entity.RenderEntityEvent;
import com.paragon.api.util.entity.EntityUtil;
import com.paragon.api.util.render.OutlineUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.api.util.string.EnumFormatter;
import com.paragon.asm.mixins.accessor.IEntityRenderer;
import com.paragon.asm.mixins.accessor.IRenderGlobal;
import com.paragon.asm.mixins.accessor.IShaderGroup;
import com.paragon.client.shader.shaders.OutlineShader;
import com.paragon.client.systems.module.Module;
import com.paragon.client.systems.module.ModuleCategory;
import com.paragon.client.systems.module.setting.Setting;
import me.wolfsurge.cerauno.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * @author Wolfsurge, with shader stuff from Cosmos (first time using shaders / glsl lel)
 */
public class ESP extends Module {

    /* Entity settings */
    private final Setting<Boolean> passive = new Setting<>("Passives", true)
            .setDescription("Highlight passive entities");

    private final Setting<Boolean> mobs = new Setting<>("Mobs", true)
            .setDescription("Highlight mobs");

    private final Setting<Boolean> players = new Setting<>("Players", true)
            .setDescription("Highlight player entities");

    private final Setting<Boolean> items = new Setting<>("Items", true)
            .setDescription("Highlight items");

    private final Setting<Boolean> crystals = new Setting<>("Crystals", true)
            .setDescription("Highlight crystals");

    // Render settings
    private final Setting<Mode> mode = new Setting<>("Mode", Mode.SHADER)
            .setDescription("How to render the entities");

    private final Setting<Float> lineWidth = new Setting<>("Line Width", 1f, 0.1f, 3f, 0.1f)
            .setDescription("How thick to render the outlines");

    // Outline shader
    private final Setting<Boolean> outline = new Setting<>("Outline", true)
            .setDescription("Outline the fill")
            .setParentSetting(mode)
            .setVisibility(() -> mode.getValue().equals(Mode.SHADER));

    private final Setting<Boolean> fill = new Setting<>("Fill", true)
            .setDescription("Fill the outline")
            .setParentSetting(mode)
            .setVisibility(() -> mode.getValue().equals(Mode.SHADER));

    private final Setting<Color> colour = new Setting<>("Colour", new Color(185, 17, 255))
            .setDescription("The colour to highlight items in");

    private Framebuffer framebuffer;
    private float lastScaleFactor, lastScaleWidth, lastScaleHeight;

    // Shaders
    private final OutlineShader outlineShader = new OutlineShader();

    public ESP() {
        super("ESP", ModuleCategory.RENDER, "Highlights entities in the world");
        this.addSettings(passive, mobs, players, items, crystals, mode, lineWidth, colour);
    }

    @Override
    public void onDisable() {
        if (nullCheck()) {
            return;
        }

        for(Entity e : mc.world.loadedEntityList) {
            e.setGlowing(false);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.HOTBAR) && mode.getValue().equals(Mode.SHADER)) {
            // Pretty much just taken from Cosmos, all credit goes to them (sorry linus!)
            // https://github.com/momentumdevelopment/cosmos/blob/main/src/main/java/cope/cosmos/client/features/modules/visual/ESPModule.java

            GlStateManager.enableAlpha();
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();

            // Delete old framebuffer
            if (framebuffer != null) {
                framebuffer.framebufferClear();

                if (lastScaleFactor != event.getResolution().getScaleFactor()|| lastScaleWidth != event.getResolution().getScaledWidth() || lastScaleHeight != event.getResolution().getScaledHeight()) {
                    framebuffer.deleteFramebuffer();
                    framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
                    framebuffer.framebufferClear();
                }

                lastScaleFactor = event.getResolution().getScaleFactor();
                lastScaleWidth = event.getResolution().getScaledWidth();
                lastScaleHeight = event.getResolution().getScaledHeight();
            } else {
                framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
            }

            framebuffer.bindFramebuffer(false);
            boolean previousShadows = mc.gameSettings.entityShadows;
            mc.gameSettings.entityShadows = false;

            ((IEntityRenderer) mc.entityRenderer).setupCamera(event.getPartialTicks(), 0);

            for (Entity entity : mc.world.loadedEntityList) {
                if (entity != null && entity != mc.player && isEntityValid(entity)) {
                    mc.getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), false);
                }
            }

            mc.gameSettings.entityShadows = previousShadows;
            GlStateManager.enableBlend();
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            framebuffer.unbindFramebuffer();
            mc.getFramebuffer().bindFramebuffer(true);
            mc.entityRenderer.disableLightmap();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.pushMatrix();

            // Render shader
            outlineShader.setColour(colour.getValue());
            outlineShader.setWidth(lineWidth.getValue());
            outlineShader.setFill(fill.getValue() ? 1 : 0);
            outlineShader.setOutline(outline.getValue() ? 1 : 0);
            outlineShader.startShader();

            mc.entityRenderer.setupOverlayRendering();

            glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
            glBegin(GL_QUADS);
            glTexCoord2d(0, 1);
            glVertex2d(0, 0);
            glTexCoord2d(0, 0);
            glVertex2d(0, event.getResolution().getScaledHeight());
            glTexCoord2d(1, 0);
            glVertex2d(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
            glTexCoord2d(1, 1);
            glVertex2d(event.getResolution().getScaledWidth(), 0);
            glEnd();

            // Stop drawing shader
            glUseProgram(0);
            glPopMatrix();

            mc.entityRenderer.enableLightmap();

            GlStateManager.popMatrix();
            GlStateManager.popAttrib();

            mc.entityRenderer.setupOverlayRendering();
        }
    }

    @Listener
    public void onRenderEntity(RenderEntityEvent event) {
        if(isEntityValid(event.getEntity()) && mode.getValue() == Mode.OUTLINE) {
            OutlineUtil.renderOne(lineWidth.getValue());
            event.renderModel();
            OutlineUtil.renderTwo();
            event.renderModel();
            OutlineUtil.renderThree();
            event.renderModel();
            OutlineUtil.renderFour(colour.getValue());
            event.renderModel();
            OutlineUtil.renderFive();
            event.renderModel();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            if(isEntityValid(e)) {
                espEntity(e);
            }
        }

        // Check glow
        if (mode.getValue().equals(Mode.GLOW)) {
            // Get shaders
            List<Shader> shaders = ((IShaderGroup) ((IRenderGlobal) mc.renderGlobal).getEntityOutlineShader()).getListShaders();

            shaders.forEach(shader -> {
                // Get line width
                ShaderUniform uniform = shader.getShaderManager().getShaderUniform("Radius");

                if (uniform != null) {
                    // Set line width
                    uniform.set(lineWidth.getValue());
                }
            });
        }
    }

    @Listener
    public void onShaderColour(ShaderColourEvent event) {
        if (mode.getValue().equals(Mode.GLOW)) {
            event.setColour(colour.getValue());
            event.cancel();
        }
    }

    @Listener
    public void onSettingUpdate(SettingUpdateEvent event) {
        if (event.getSetting() == mode) {
            for (Entity entity : mc.world.loadedEntityList) {
                entity.setGlowing(false);
            }
        }
    }

    /**
     * Highlights an entity
     * @param entityIn The entity to highlight
     */
    public void espEntity(Entity entityIn) {
        if (mode.getValue().equals(Mode.BOX)) {
            RenderUtil.drawBoundingBox(EntityUtil.getEntityBox(entityIn), lineWidth.getValue(), colour.getValue());
        } else if (mode.getValue().equals(Mode.GLOW)) {
            entityIn.setGlowing(true);
        }
    }

    /**
     * Checks if an entity is valid
     * @param entityIn The entity to check
     * @return Is the entity valid
     */
    private boolean isEntityValid(Entity entityIn) {
        return entityIn instanceof EntityPlayer && entityIn != mc.player && players.getValue() || entityIn instanceof EntityLiving && !(entityIn instanceof EntityMob) && passive.getValue() || entityIn instanceof EntityMob && mobs.getValue() || entityIn instanceof EntityEnderCrystal && crystals.getValue() || entityIn instanceof EntityItem && items.getValue();
    }

    @Override
    public String getArrayListInfo() {
        return " " + EnumFormatter.getFormattedText(mode.getValue());
    }

    public enum Mode {
        /**
         * Outline the entity
         */
        OUTLINE,

        /**
         * Apply vanilla glow shader
         */
        GLOW,

        /**
         * Draw a box
         */
        BOX,

        /**
         * Draw with shader
         */
        SHADER
    }
}
