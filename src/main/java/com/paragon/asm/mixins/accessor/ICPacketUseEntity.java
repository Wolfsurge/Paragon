package com.paragon.asm.mixins.accessor;

import net.minecraft.network.play.client.CPacketUseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketUseEntity.class)
public interface ICPacketUseEntity {

    @Accessor("entityId")
    void setEntityID(int id);

    @Accessor("action")
    void setAction(CPacketUseEntity.Action action);

}
