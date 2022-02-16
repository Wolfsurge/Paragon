package com.paragon.asm.mixins.accessor;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Wolfsurge
 */
@Mixin(SPacketEntityVelocity.class)
public interface ISPacketEntityVelocity {

    @Accessor("motionX")
    void setMotionX(int motionX);

    @Accessor("motionY")
    void setMotionY(int motionY);

    @Accessor("motionZ")
    void setMotionZ(int motionZ);

}
