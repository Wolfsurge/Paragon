package com.paragon.client.managers.rotation;

import com.paragon.api.util.Wrapper;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author Wolfsurge
 */
public class Rotation implements Wrapper {

    // Yaw and pitch of rotation
    private float yaw, pitch;
    // Type of rotation
    private RotationType rotationType;

    public Rotation(float yaw, float pitch, RotationType rotationType) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.rotationType = rotationType;
    }

    public void doRotation() {
        switch (rotationType) {
            case PACKET:
                mc.getConnection().sendPacket(new CPacketPlayer.Rotation(getYaw(), getPitch(), mc.player.onGround));
                break;
            case LEGIT:
                mc.player.rotationYaw = getYaw();
                mc.player.rotationPitch = getPitch();
                break;
            default:
                break;
        }
    }

    /**
     * Gets the yaw
     * @return The yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Gets the pitch
     * @return The pitch
     */
    public float getPitch() {
        return pitch;
    }
    /**
     * Gets the rotation type
     * @return The rotation type
     */

    public RotationType getRotationType() {
        return rotationType;
    }
}
