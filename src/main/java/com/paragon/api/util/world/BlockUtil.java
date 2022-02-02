package com.paragon.api.util.world;

import com.paragon.api.util.miscellaneous.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class BlockUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    /**
     * Gets blocks around the player
     * @param player The player
     * @param blockRange The range to get blocks
     * @return A list of blocks
     */
    public static List<BlockPos> getBlocksAroundPlayer(EntityPlayer player, double blockRange) {
        List<BlockPos> nearbyBlocks = new ArrayList<>();
        int range = (int) MathUtils.roundDouble(blockRange, 0);

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range - (range / 2); y++)
                for (int z = -range; z <= range; z++)
                    nearbyBlocks.add(player.getPosition().add(x, y, z));

        return nearbyBlocks;
    }

    /**
     * Gets the block at a position
     * @param pos The position
     * @return The block
     */
    public static Block getBlockAtPos(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    /**
     * Gets the bounding box of a block
     * @param blockPos The block
     * @return The bounding box of the entity
     */
    public static AxisAlignedBB getBlockBox(BlockPos blockPos) {
        double x = blockPos.getX() - mc.getRenderManager().viewerPosX;
        double y = blockPos.getY() - mc.getRenderManager().viewerPosY;
        double z = blockPos.getZ() - mc.getRenderManager().viewerPosZ;

        return new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
    }

    /**
     * Returns whether an entity is on top / in a block, and therefore cannot be placed on
     * @param blockPos The block to check
     * @return Whether the block is placeable
     */
    public static boolean isIntercepted(BlockPos blockPos) {
        for(Entity entity : mc.world.loadedEntityList) {
            if (new AxisAlignedBB(blockPos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }

        return false;
    }

}
