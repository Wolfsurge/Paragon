package com.paragon.client.features.module.impl.movement;

import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (nullCheck()) {
            return;
        }

        mc.player.capabilities.isFlying = true;
    }

}
