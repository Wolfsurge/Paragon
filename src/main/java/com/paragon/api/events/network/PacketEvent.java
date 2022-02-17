package com.paragon.api.events.network;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Wolfsurge
 */
@Cancelable
public class PacketEvent extends Event {

    // The packet being sent
    private final Packet packet;

    public PacketEvent(Packet packet) {
        this.packet = packet;
    }

    public static class PreSend extends PacketEvent {
        public PreSend(Packet packet) {
            super(packet);
        }
    }

    public static class PostSend extends PacketEvent {
        public PostSend(Packet packet) {
            super(packet);
        }
    }

    public static class PreReceive extends PacketEvent {
        public PreReceive(Packet packet) {
            super(packet);
        }
    }

    public static class PostReceive extends PacketEvent {
        public PostReceive(Packet packet) {
            super(packet);
        }
    }

    /**
     * Gets the packet being sent or received
     * @return The packet
     */
    public Packet getPacket() {
        return packet;
    }
}
