package com.paragon.api.event;

import com.paragon.api.event.events.RenderGUIEvent;
import com.paragon.api.event.events.UpdateEvent;
import com.paragon.client.Paragon;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.xml.PrettyPrinter;

public class EventParser {

    public static EventParser INSTANCE = new EventParser();

    public void initialise() {
        MinecraftForge.EVENT_BUS.register(this);
        Paragon.EVENT_BUS.subscribe(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null) return;

        Paragon.EVENT_BUS.post(new UpdateEvent.Client());
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if(Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null) return;

        Paragon.EVENT_BUS.post(new UpdateEvent.Server());
    }

    @SubscribeEvent
    public void onBothTick(TickEvent event) {
        if(Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null) return;

        Paragon.EVENT_BUS.post(new UpdateEvent.Both());
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
            Paragon.EVENT_BUS.post(new RenderGUIEvent());
    }

}
