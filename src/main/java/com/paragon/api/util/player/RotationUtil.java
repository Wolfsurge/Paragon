package com.paragon.api.util.player;

import net.minecraft.util.math.BlockPos;
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
        float length = (float) Math.hypot(vec3d.x, vec3d.y);
        float yaw = normaliseAngle((float) (Math.toDegrees(Math.atan2(vec3d.z, vec3d.x)) - 90));
        float pitch = normaliseAngle((float) (Math.toDegrees(Math.atan2(vec3d.y, length)) - 90));

        return new Vec2f(yaw, pitch);
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
