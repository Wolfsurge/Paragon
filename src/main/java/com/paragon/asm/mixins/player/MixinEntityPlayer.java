package com.paragon.asm.mixins.player;

import com.paragon.api.events.player.TravelEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(float strafe, float vertical, float forward, CallbackInfo ci) {
        TravelEvent travelEvent = new TravelEvent(strafe, vertical, forward);
        MinecraftForge.EVENT_BUS.post(travelEvent);

        if (travelEvent.isCanceled()) {
            ci.cancel();
        }
    }

}
