package com.paragon.api.util;

import net.minecraft.client.Minecraft;

/**
 * @author Wolfsurge
 */
public interface Wrapper {

    // Minecraft instance
    Minecraft mc = Minecraft.getMinecraft();

    /**
     * Checks if the player and world are null
     * @return If the player and world are null
     */
    default boolean nullCheck() {
        return mc.player == null && mc.world == null;
    }
}
