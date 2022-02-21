package com.paragon.client.features.module.impl.movement;

import com.paragon.api.events.player.TravelEvent;
import com.paragon.api.util.miscellaneous.BooleanUtil;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ElytraFlight extends Module {

    private ModeSetting mode = new ModeSetting("Mode", "What mode to use", "Control", new String[]{"Control", "Packet", "Strict", "Boost"});
    private NumberSetting strictPitch = (NumberSetting) new NumberSetting("Pitch", "The locked pitch to use for strict", 0, -180, 180, 1).setParentSetting(mode).setVisiblity(() -> mode.is("Strict"));
    private BooleanSetting y = (BooleanSetting) new BooleanSetting("Y", "Lets you control your Y", true).setParentSetting(mode).setVisiblity(() -> mode.is("Control"));

    private NumberSetting speed = new NumberSetting("Speed", "The speed of flight", 1, 0.1f, 2, 0.1f);

    public ElytraFlight() {
        super("ElytraFlight", "Allows for easier flight with elytra", Category.MOVEMENT);
        this.addSettings(mode, speed);
    }

    @EventHandler
    private final Listener<TravelEvent> travelEventListener = new Listener<>(event -> {
        if (nullCheck()) {
            return;
        }

        if (mc.player.isElytraFlying()) {
            event.cancel();

            float forward = mc.player.movementInput.moveForward;
            float strafe = mc.player.movementInput.moveStrafe;
            float yaw = mc.player.rotationYaw;

            if (forward != 0) {
                if (strafe >= 1) {
                    yaw += (float) (forward > 0 ? -45 : 45);
                    strafe = 0;
                }

                else if (strafe <= -1) {
                    yaw += (float) (forward > 0 ? 45 : -45);
                    strafe = 0;
                }

                if (forward > 0)
                    forward = 1;

                else if (forward < 0)
                    forward = -1;
            }

            double sin = Math.sin(Math.toRadians(yaw + 90));
            double cos = Math.cos(Math.toRadians(yaw + 90));

            mc.player.motionX = (double) forward * speed.getValue() * cos + (double) strafe * speed.getValue() * sin;
            mc.player.motionZ = (double) forward * speed.getValue() * sin - (double) strafe * speed.getValue() * cos;
            mc.player.stepHeight = 0.5f;

            if ((!(mc.player.moveForward != 0 || mc.player.moveStrafing != 0))) {
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
            }
        }
    });


}
