package com.paragon.api.event.render.world;

import me.wolfsurge.cerauno.event.CancellableEvent;
import net.minecraft.util.math.BlockPos;

/**
 * @author Wolfsurge
 */
public class RenderBlockSmoothEvent extends CancellableEvent {

    private final BlockPos pos;

    public RenderBlockSmoothEvent(BlockPos pos) {
        this.pos = pos;
    }

    /**
     * Gets the pos
     * @return The pos
     */
    public BlockPos getPos() {
        return pos;
    }

}