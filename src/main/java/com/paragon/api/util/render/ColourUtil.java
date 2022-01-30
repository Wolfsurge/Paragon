package com.paragon.api.util.render;

import java.awt.*;

public class ColourUtil {

    public static int getRainbow(float time, float saturation, int index) {
        float hue = ((System.currentTimeMillis() + index) % (int) (time * 1000) / (time * 1000));
        return Color.HSBtoRGB(hue, saturation, 1);
    }

}
