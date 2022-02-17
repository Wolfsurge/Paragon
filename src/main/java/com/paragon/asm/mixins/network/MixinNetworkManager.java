package com.paragon.asm.mixins.network;

import com.paragon.api.events.network.PacketEvent;
import com.paragon.client.Paragon;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Wolfsurge
 */

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onPacketSendPre(Packet<?> packetIn, CallbackInfo ci) {
        PacketEvent.PreSend preSend = new PacketEvent.PreSend(packetIn);
        MinecraftForge.EVENT_BUS.post(preSend);

        if (preSend.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("TAIL"))
    public void onPacketSendPost(Packet<?> packetIn, CallbackInfo ci) {
        PacketEvent.PostSend postSend = new PacketEvent.PostSend(packetIn);
        MinecraftForge.EVENT_BUS.post(postSend);
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onPacketReceivePre(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo ci) {
        PacketEvent.PreReceive preReceive = new PacketEvent.PreReceive(p_channelRead0_2_);
        MinecraftForge.EVENT_BUS.post(preReceive);

        if (preReceive.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("TAIL"), cancellable = true)
    public void onPacketReceivePost(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo ci) {
        PacketEvent.PostReceive postReceive = new PacketEvent.PostReceive(p_channelRead0_2_);
        MinecraftForge.EVENT_BUS.post(postReceive);
    }
}
