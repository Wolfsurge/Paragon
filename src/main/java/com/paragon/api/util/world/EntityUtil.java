package com.paragon.api.util.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

public class EntityUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    /**
     * Gets the interpolated position of a given entity
     * @param entityIn The given entity
     * @return The interpolated position
     */
    public static Vec3d getInterpolatedPosition(Entity entityIn) {
        return new Vec3d(entityIn.lastTickPosX, entityIn.lastTickPosY, entityIn.lastTickPosZ).add(getInterpolatedAmount(entityIn, mc.getRenderPartialTicks()));
    }

    /**
     * Gets the interpolated amount of the entity
     * @param entity The entity in
     * @param partialTicks The render partial ticks
     * @return The interpolated amount
     */
    public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * partialTicks, (entity.posY - entity.lastTickPosY) * partialTicks, (entity.posZ - entity.lastTickPosZ) * partialTicks);
    }

    /**
     * Gets the text formatting colour based on an player's health
     * @param player The player in
     * @return The colour of the health
     */
    public static TextFormatting getTextColourFromEntityHealth(EntityPlayer player) {
        if(player.getHealth() <= 20 && player.getHealth() > 15)
            return TextFormatting.GREEN;
        else if(player.getHealth() <= 15 && player.getHealth() > 10)
            return TextFormatting.GOLD;
        else if(player.getHealth() <= 10 && player.getHealth() > 5)
            return TextFormatting.RED;
        else if(player.getHealth() <= 5)
            return TextFormatting.DARK_RED;

        return TextFormatting.GRAY;
    }

    /**
     * Gets the bounding box of an entity
     * @param entity The entity
     * @return The bounding box of the entity
     */
    public static AxisAlignedBB getEntityBox(Entity entity) {
        return new AxisAlignedBB(
                entity.getEntityBoundingBox().minX - 0.05 - entity.posX + (entity.posX - mc.getRenderManager().viewerPosX),
                entity.getEntityBoundingBox().minY - entity.posY + (entity.posY - mc.getRenderManager().viewerPosY),
                entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + (entity.posZ - mc.getRenderManager().viewerPosZ),
                entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + (entity.posX - mc.getRenderManager().viewerPosX),
                entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + (entity.posY - mc.getRenderManager().viewerPosY),
                entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + (entity.posZ - mc.getRenderManager().viewerPosZ)
        );
    }

}
