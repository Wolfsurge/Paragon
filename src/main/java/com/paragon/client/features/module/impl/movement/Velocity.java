package com.paragon.client.features.module.impl.movement;

import com.paragon.api.events.network.PacketEvent;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {

    private final BooleanSetting velocityPacket = new BooleanSetting("Velocity Packet", "Cancels or modifies the velocity packet", true);
    private final BooleanSetting explosions = new BooleanSetting("Explosions", "Cancels or modifies the explosion knockback", true);

    private final NumberSetting horizontal = new NumberSetting("Horizontal", "The horizontal modifier", 0, 0, 100, 1);
    private final NumberSetting vertical = new NumberSetting("Vertical", "The vertical modifier", 0, 0, 100, 1);

    public Velocity() {
        super("Velocity", "Stops crystals and mobs from causing you knockback", Category.MOVEMENT);
        this.addSettings(velocityPacket, explosions, horizontal, vertical);
    }

    @EventHandler
    private final Listener<PacketEvent.PreReceive> preReceiveListener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketEntityVelocity && velocityPacket.isEnabled()) {
            if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                    event.cancel();
                } else {
                    int motionX = ((SPacketEntityVelocity) event.getPacket()).motionX / 100;
                    int motionY = ((SPacketEntityVelocity) event.getPacket()).motionY / 100;
                    int motionZ = ((SPacketEntityVelocity) event.getPacket()).motionZ / 100;

                    ((SPacketEntityVelocity) event.getPacket()).motionX = (int) (motionX * horizontal.getValue());
                    ((SPacketEntityVelocity) event.getPacket()).motionY = (int) (motionY * vertical.getValue());
                    ((SPacketEntityVelocity) event.getPacket()).motionZ = (int) (motionZ * horizontal.getValue());
                }
            }
        }

        if (event.getPacket() instanceof SPacketExplosion && explosions.isEnabled()) {
            event.cancel();
        }
    });

    @Override
    public String getModuleInfo() {
        return " H% " + horizontal.getValue() + ", V% " + vertical.getValue();
    }
}
