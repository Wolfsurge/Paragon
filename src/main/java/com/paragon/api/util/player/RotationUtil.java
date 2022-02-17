package com.paragon.api.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {

    /**
     * Gets the rotation to a block position
     * @param pos The block position to calculate angles to
     * @return The calculated angles
     */
    public static Vec2f getRotationToBlockPos(BlockPos pos) {
        return getRotationToVec3d(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    /**
     * Gets the yaw and pitch to rotate to a Vec3D
     * @param vec3d The Vec3D to calculate rotations to
     * @return A Vec2f of the angles
     */
    public static Vec2f getRotationToVec3d(Vec3d vec3d) {
        // find the yaw and pitch to the vector
        Minecraft mc = Minecraft.getMinecraft();
        float yaw = (float) (Math.toDegrees(Math.atan2(vec3d.subtract(mc.player.getPositionEyes(1)).z, vec3d.subtract(mc.player.getPositionEyes(1)).x)) - 90);
        float pitch = (float) Math.toDegrees(-Math.atan2(vec3d.subtract(mc.player.getPositionEyes(1)).y, Math.hypot(vec3d.subtract(mc.player.getPositionEyes(1)).x, vec3d.subtract(mc.player.getPositionEyes(1)).z)));

        // wrap the degrees to values between -180 and 180
        return new Vec2f(MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch));
    }

    /**
     * Normalises an angle
     * @param angle The angle to normalise
     * @return The normalised angle
     */
    public static float normaliseAngle(float angle) {
        angle %= 360.0;

        if (angle >= 180.0) {
            angle -= 360.0;
        }

        if (angle < -180.0) {
            angle += 360.0;
        }

        return angle;
    }

}
