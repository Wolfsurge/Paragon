package com.paragon.api.util.render;

import com.paragon.api.util.Wrapper;
import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.world.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil implements Wrapper {

    /**
     * Draws a rectangle at the given coordinates
     * @param x The X (left) coord
     * @param y The Y (top) coord
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param colour The colour of the rectangle
     */
    public static void drawRect(float x, float y, float width, float height, int colour) {
        float c = (float) (colour >> 24 & 255) / 255.0F;
        float c1 = (float) (colour >> 16 & 255) / 255.0F;
        float c2 = (float) (colour >> 8 & 255) / 255.0F;
        float c3 = (float) (colour & 255) / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x + width, y, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(x, y, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(x, y + height, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(x + width, y + height, 0).color(c1, c2, c3, c).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    /**
     * Draws a rectangle at the given coordinates
     * @param x The X (left) coord
     * @param y The Y (top) coord
     * @param x2 The second X (right) coord
     * @param y2 The second Y (bottom) coord
     * @param colour The colour of the rectangle
     */
    public static void drawSelectRect(float x, float y, float x2, float y2, int colour) {
        float c = (float) (colour >> 24 & 255) / 255.0F;
        float c1 = (float) (colour >> 16 & 255) / 255.0F;
        float c2 = (float) (colour >> 8 & 255) / 255.0F;
        float c3 = (float) (colour & 255) / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x2, y, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(x, y, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(x, y2, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(x2, y2, 0).color(c1, c2, c3, c).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    /**
     * Draws a line from one pos to another
     * @param x1 Start X
     * @param y1 Start Y
     * @param z1 Start Z
     * @param x2 End X
     * @param y2 End Y
     * @param z2 End Z
     * @param color The colour of the line
     * @param disableDepth Disable GL depth
     * @param lineWidth Width of the line
     */
    public static void drawLine3D(double x1, double y1, double z1, double x2, double y2, double z2, int color, boolean disableDepth, float lineWidth) {
        // Enable 3D render
        if(disableDepth) {
            glDisable(GL_ALPHA_TEST);
            glEnable(GL_BLEND);
        }
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

        ColourUtil.setColor(color);
        glLineWidth(lineWidth);
        glBegin(1);
        glVertex3d(x1, y1, z1);
        glVertex3d(x2, y2, z2);
        glEnd();

        // Disable 3D render
        if (disableDepth) {
            GL11.glDepthMask(true);
            GL11.glEnable(GL_DEPTH_TEST);
        }
        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL_BLEND);
        GL11.glEnable(GL_ALPHA_TEST);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Draws a tracer to a given entity
     * @param e The entity to draw a line to
     * @param lineWidth The width of the line
     * @param col The colour of the line
     */
    public static void drawTracer(Entity e, float lineWidth, Color col) {
        Vec3d vec = EntityUtil.getInterpolatedPosition(e);
        double x = vec.x - mc.getRenderManager().viewerPosX;
        double y = vec.y - mc.getRenderManager().viewerPosY;
        double z = vec.z - mc.getRenderManager().viewerPosZ;

        Vec3d eyes = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch(-((float)Math.toRadians(mc.player.rotationPitch))).rotateYaw( -((float)Math.toRadians(mc.player.rotationYaw)));

        if(col.getAlpha() == 0) return;

        drawLine3D(eyes.x, eyes.y + mc.player.getEyeHeight(), eyes.z, x, y + (e.height / 2), z, col.getRGB(), true, lineWidth);
    }

    /**
     * Starts scissoring a rect
     * @param x X coord
     * @param y Y coord
     * @param width Width of scissor
     * @param height Height of scissor
     */
    public static void startGlScissor(double x, double y, double width, double height) {
        glPushAttrib(GL_SCISSOR_BIT);
        {
            scissorRect(x, y, width, height);
            glEnable(GL_SCISSOR_TEST);
        }
    }

    /**
     * Disables scissor
     */
    public static void endGlScissor() {
        glDisable(GL_SCISSOR_TEST);
        glPopAttrib();
    }

    /**
     * Scissors a rect
     * @param x X coord
     * @param y Y coord
     * @param width Width of scissor
     * @param height Height of scissor
     */
    public static void scissorRect(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        final double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        GL11.glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

    /**
     * Draws a gradient rect. From W +3, for the colour pickers.
     * @param mx Min X
     * @param my Min Y
     * @param max Max X
     * @param may Max Y
     * @param startColour Start colour of the rect
     * @param endColour End colour of the rect
     */
    public static void leftGradient(double mx, double my, double max, double may, int startColour, int endColour) {
        float minX = (float) mx;
        float minY = (float) my;
        float maxX = (float) max;
        float maxY = (float) may;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_POLYGON);
        glColor4f((startColour >> 16 & 0xFF) / 255.0f, (startColour >> 8 & 0xFF) / 255.0f, (startColour & 0xFF) / 255.0f, (startColour >> 24 & 0xFF) / 255.0f);
        glVertex2f(minX, minY);
        glVertex2f(minX, maxY);
        glColor4f((endColour >> 16 & 0xFF) / 255.0f, (endColour >> 8 & 0xFF) / 255.0f, (endColour & 0xFF) / 255.0f, (endColour >> 24 & 0xFF) / 255.0f);
        glVertex2f(maxX, maxY);
        glVertex2f(maxX, minY);
        glEnd();
        glShadeModel(GL_FLAT);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    /**
     * Draws a bounding box around an AABB
     * @param axisAlignedBB The AABB
     * @param lineThickness The line width
     * @param colour The colour of the outline
     */
    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB, float lineThickness, Color colour) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineThickness);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        RenderGlobal.drawSelectionBoundingBox(axisAlignedBB, colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, colour.getAlpha() / 255f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Draws a filled box around an AABB
     * @param axisAlignedBB The AABB
     * @param colour The colour of the outline
     */
    public static void drawFilledBox(AxisAlignedBB axisAlignedBB, Color colour) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        RenderGlobal.renderFilledBox(axisAlignedBB, colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, colour.getAlpha() / 255f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawNametagText(String text, Vec3d location, int textColour) {
        GlStateManager.pushMatrix();
        // Translate
        float scale = 0.02666667f;

        GlStateManager.translate(location.x - mc.getRenderManager().viewerPosX, location.y - mc.getRenderManager().viewerPosY, location.z - mc.getRenderManager().viewerPosZ);
        GlStateManager.glNormal3f(0, 1, 0);
        GlStateManager.rotate(-mc.player.rotationYaw, 0, 1, 0);

        // Rotate based on the view
        GlStateManager.rotate(mc.player.rotationPitch, (mc.gameSettings.thirdPersonView == 2) ? -1 : 1, 0, 0);
        GlStateManager.scale(-scale, -scale, scale);

        GlStateManager.disableDepth();
        GlStateManager.translate(-(getStringWidth(text) / 2f), 0, 0);

        renderText(text, 0, 0, textColour);
        GlStateManager.popMatrix();
    }

    static void renderText(String text, float x, float y, int colour) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, colour);
    }

    static float getStringWidth(String text) {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

}