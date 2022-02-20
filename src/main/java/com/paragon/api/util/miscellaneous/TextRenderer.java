package com.paragon.api.util.miscellaneous;

import net.minecraft.client.Minecraft;

public interface TextRenderer {

    default void renderText(String text, float x, float y, int colour) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, colour);
    }

    default void renderCenteredText(String text, float x, float y, int colour) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x - (getStringWidth(text) / 2f), y, colour);
    }

    default float getStringWidth(String text) {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

}
