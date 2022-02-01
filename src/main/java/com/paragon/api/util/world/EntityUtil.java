package com.paragon.api.util.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class EntityUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static Vec3d getInterpolatedPosition(Entity entityIn, float renderPartialTicks) {
        return new Vec3d(entityIn.lastTickPosX, entityIn.lastTickPosY, entityIn.lastTickPosZ).add(getInterpolatedAmount(entityIn, mc.getRenderPartialTicks()));
    }

    public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * partialTicks, (entity.posY - entity.lastTickPosY) * partialTicks, (entity.posZ - entity.lastTickPosZ) * partialTicks);
    }

}
