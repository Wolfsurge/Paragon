package com.paragon.client.features.module.impl.movement;

import com.paragon.api.event.events.UpdateEvent;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import org.lwjgl.input.Keyboard;

public class Step extends Module {

    private final NumberSetting stepHeight = new NumberSetting("Step Height", "How high to step up", 1.5f, 0.5f, 2.5f, 0.5f);

    public Step() {
        super("Step", "Lets you instantly step up blocks", Category.MOVEMENT);
        this.addSettings(stepHeight);
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5f;
    }

    @EventHandler
    private final Listener<UpdateEvent.Client> updateEventListener = new Listener<>(updateEvent -> {
        mc.player.stepHeight = stepHeight.getValue();
    });

}
