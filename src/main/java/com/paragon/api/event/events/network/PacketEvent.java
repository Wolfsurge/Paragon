package com.paragon.api.event.events.network;

import com.paragon.api.event.Event;
import net.minecraft.network.Packet;

/**
 * @author Wolfsurge
 */
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
