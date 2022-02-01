package com.paragon.client.features.module.impl.movement;

import com.paragon.api.event.events.UpdateEvent;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

public class Step extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "What mode to use", "NCP", new String[]{"NCP", "Vanilla"});
    private final NumberSetting stepHeight = new NumberSetting("Step Height", "How high to step up", 1.5f, 0.5f, 2.5f, 0.5f);

    public Step() {
        super("Step", "Lets you instantly step up blocks", Category.MOVEMENT);
        this.addSettings(mode, stepHeight);
    }

    @Override
    public void onDisable() {
        if(mode.is("Vanilla"))
            mc.player.stepHeight = 0.5f;
    }

    @EventHandler
    private final Listener<UpdateEvent> updateEventListener = new Listener<>(updateEvent -> {
        if(updateEvent.getUpdateType() != UpdateEvent.Type.Client) return;

        if(mode.is("Vanilla"))
            mc.player.stepHeight = stepHeight.getValue();
        else if(mode.is("NCP")) {
            if (mc.player.collidedHorizontally && mc.player.onGround && mc.player.fallDistance == 0.0f && !mc.player.isOnLadder() && !mc.player.movementInput.jump) {
                AxisAlignedBB box = mc.player.getEntityBoundingBox().offset(0.0, 0.05, 0.0).grow(0.05);
                if (!mc.world.getCollisionBoxes(mc.player, box.offset(0.0, 1.0, 0.0)).isEmpty()) {
                    return;
                }

                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ);
            }
        }
    });

}
