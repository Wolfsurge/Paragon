package com.paragon.api.util.render;

import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor4f;

public class ColourUtil {

    /**
     * Creates a rainbow wave
     * @param time How long for each wave
     * @param saturation The saturation of the colour
     * @param addition How much hue to add to the wave
     * @return A rainbow in the RGB format
     */
    public static int getRainbow(float time, float saturation, int addition) {
        float hue = ((System.currentTimeMillis() + addition) % (int) (time * 1000) / (time * 1000));
        return Color.HSBtoRGB(hue, saturation, 1);
    }

    /**
     * Sets the GL colour based on a hex integer
     * @param colourHex The integer of the hex value
     */
    public static void setColour(int colourHex) {
        float alpha = (colourHex >> 24 & 0xFF) / 255.0F;
        float red = (colourHex >> 16 & 0xFF) / 255.0F;
        float green = (colourHex >> 8 & 0xFF) / 255.0F;
        float blue = (colourHex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, (alpha == 0.0F) ? 1.0F : alpha);
    }

    /**
     * Integrates alpha into a colour
     * @param colour The original colour
     * @param alpha The new alpha
     * @return The new colour
     */
    public static Color integrateAlpha(Color colour, float alpha) {
        float red = colour.getRed() / 255f;
        float green = colour.getGreen() / 255f;
        float blue = colour.getBlue() / 255f;
        return new Color(red, green, blue, alpha);
    }

}
