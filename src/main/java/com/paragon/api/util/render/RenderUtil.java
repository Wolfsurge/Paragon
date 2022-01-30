package com.paragon.api.util.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;

public class RenderUtil {

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

    public static void drawTexturedModalRect(float x, float y, float textureX, float textureY, float width, float height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((x + 0), y + height, 0).tex((textureX + 0) * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos((x + width), (y + height), 0).tex((textureX + width) * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos((x + width), (y + 0), 0).tex((textureX + width) * 0.00390625F, (textureY + 0) * 0.00390625F).endVertex();
        bufferbuilder.pos((x + 0), (y + 0), 0).tex((textureX + 0) * 0.00390625F, (textureY + 0) * 0.00390625F).endVertex();
        tessellator.draw();
    }

}
