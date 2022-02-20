package com.paragon.client.features.module.impl.misc;

import com.paragon.api.util.player.EntityFakePlayer;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;

public class FakePlayer extends Module {

    public FakePlayer() {
        super("FakePlayer", "Spawns a fake client side player", Category.MISC);
    }

    private EntityFakePlayer fakePlayer;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            return;
        }

        fakePlayer = new EntityFakePlayer();
    }

    @Override
    public void onDisable() {
        if (fakePlayer != null) {
            fakePlayer.despawn();
        }
    }
}
