package com.paragon.api.util.render;

public class GuiUtil {
    public static boolean mouseOver(double minX, double minY, double maxX, double maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }
}
