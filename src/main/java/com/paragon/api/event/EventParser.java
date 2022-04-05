package com.paragon.api.event;

import com.paragon.Paragon;
import com.paragon.api.event.network.PacketEvent;
import com.paragon.api.event.combat.TotemPopEvent;
import com.paragon.api.util.Wrapper;
import me.wolfsurge.cerauno.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.common.MinecraftForge;

public class EventParser implements Wrapper {

    public EventParser() {
        MinecraftForge.EVENT_BUS.register(this);
        Paragon.INSTANCE.getEventBus().register(this);
    }

}