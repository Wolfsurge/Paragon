package com.paragon.client.features.module.impl.movement;

import com.paragon.api.event.events.network.PacketEvent;
import com.paragon.asm.mixins.accessor.ISPacketEntityVelocity;
import com.paragon.asm.mixins.accessor.ISPacketExplosion;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {

    private final BooleanSetting velocityPacket = new BooleanSetting("Velocity Packet", "Cancels or modifies the velocity packet", true);
    private final NumberSetting vX = (NumberSetting) new NumberSetting("X", "The X modifier", 0, 0, 100, 1).setParentSetting(velocityPacket);
    private final NumberSetting vY = (NumberSetting) new NumberSetting("Y", "The Y modifier", 0, 0, 100, 1).setParentSetting(velocityPacket);
    private final NumberSetting vZ = (NumberSetting) new NumberSetting("Z", "The Z modifier", 0, 0, 100, 1).setParentSetting(velocityPacket);

    private final BooleanSetting explosions = new BooleanSetting("Explosions", "Cancels or modifies the explosion knockback", true);
    private final NumberSetting eX = (NumberSetting) new NumberSetting("X", "The X modifier", 0, 0, 100, 1).setParentSetting(explosions);
    private final NumberSetting eY = (NumberSetting) new NumberSetting("Y", "The Y modifier", 0, 0, 100, 1).setParentSetting(explosions);
    private final NumberSetting eZ = (NumberSetting) new NumberSetting("Z", "The Z modifier", 0, 0, 100, 1).setParentSetting(explosions);

    public Velocity() {
        super("Velocity", "Stops crystals and mobs from causing you knockback", Category.MOVEMENT);
        this.addSettings(velocityPacket, explosions);
    }

    @EventHandler
    private final Listener<PacketEvent.PreReceive> preReceiveListener = new Listener<>(event -> {
       if (event.getPacket() instanceof SPacketEntityVelocity && velocityPacket.isEnabled()) {
           if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
               event.cancel();
           }
       }

       if (event.getPacket() instanceof SPacketExplosion && explosions.isEnabled()) {
           event.cancel();
       }
    });

}
