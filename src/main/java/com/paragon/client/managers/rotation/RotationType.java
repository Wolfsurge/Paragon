package com.paragon.client.managers.rotation;

/**
 * @author Wolfsurge
 */
public enum RotationType {
    /**
     * Send a packet to rotate, not visible client-side
     */
    PACKET,
    /**
     * Actually rotate the player's yaw and pitch
     */
    LEGIT,
    /**
     * Do not rotate
     */
    NONE
}
