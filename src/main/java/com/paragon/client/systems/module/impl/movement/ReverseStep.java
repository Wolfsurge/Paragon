package com.paragon.client.systems.module.impl.movement;

import com.paragon.api.util.player.PlayerUtil;
import com.paragon.client.systems.module.Module;
import com.paragon.client.systems.module.ModuleCategory;

/**
 * @author Wolfsurge
 */
public class ReverseStep extends Module {

    public ReverseStep() {
        super("ReverseStep", ModuleCategory.MOVEMENT, "Moves you down when you walk off of a block");
    }

    @Override
    public void onTick() {
        if (nullCheck()) {
            return;
        }

        // Check that we want to fall
        if (mc.player.onGround && !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.isOnLadder() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY = -10.0;
        }
    }
}