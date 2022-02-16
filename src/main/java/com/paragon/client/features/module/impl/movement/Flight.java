package com.paragon.client.features.module.impl.movement;

import com.paragon.api.event.events.UpdateEvent;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class Flight extends Module {

    private final BooleanSetting parentSettingTest = new BooleanSetting("Parent Setting", "Test parent setting", true);

    public Flight() {
        super("Flight", "Lets you fly in survival mode", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        if(nullCheck()) return;

        mc.player.capabilities.isFlying = false;
    }

    @EventHandler
    private final Listener<UpdateEvent> updateEventListener = new Listener<>(event -> {
        mc.player.capabilities.isFlying = true;
    });

}
