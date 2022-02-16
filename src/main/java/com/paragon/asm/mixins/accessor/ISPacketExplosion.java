package com.paragon.asm.mixins.accessor;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Wolfsurge
 */
@Mixin(SPacketExplosion.class)
public interface ISPacketExplosion {

    @Accessor("motionX")
    void setMotionX(float motionX);

    @Accessor("motionY")
    void setMotionY(float motionY);

    @Accessor("motionZ")
    void setMotionZ(float motionZ);

}
