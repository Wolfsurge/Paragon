package com.paragon.client.features.module.impl.movement;

import com.paragon.api.event.events.UpdateEvent;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class Flight extends Module {

    public Flight() {
        super("Flight", "Lets you fly in survival mode", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
    }

    @EventHandler
    private final Listener<UpdateEvent.Client> updateEventListener = new Listener<>(event -> {
        mc.player.capabilities.isFlying = true;
    });

}
