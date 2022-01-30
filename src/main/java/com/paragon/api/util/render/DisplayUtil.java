package com.paragon.api.util.render;

import java.awt.*;

/**
 * @author Wolfsurge
 * @since 30/01/22
 */
public class DisplayUtil {
    /**
     * Gets the actual width of the monitor used
     * @return The actual width of the monitor used
     */
    public static float getDisplayWidth() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        return (float) (size.getWidth() / 2);
    }

    public static float getDisplayHeight() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        return (float) size.getHeight() / 2;
    }
}
