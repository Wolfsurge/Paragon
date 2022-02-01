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
     * Sets the GL colour based on an hex integer
     * @param colorHex The integer of the hex value
     */
    public static void setColor(int colorHex) {
        float alpha = (colorHex >> 24 & 0xFF) / 255.0F;
        float red = (colorHex >> 16 & 0xFF) / 255.0F;
        float green = (colorHex >> 8 & 0xFF) / 255.0F;
        float blue = (colorHex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, (alpha == 0.0F) ? 1.0F : alpha);
    }

}
