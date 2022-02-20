package com.paragon.api.util.player;

import com.paragon.api.util.Wrapper;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class EntityFakePlayer extends EntityOtherPlayerMP implements Wrapper {

    public EntityFakePlayer() {
        super(mc.world, mc.player.getGameProfile());
        this.copyLocationAndAnglesFrom(mc.player);
        this.inventory.copyInventory(mc.player.inventory);
        mc.world.addEntityToWorld(Integer.MAX_VALUE, this);
    }

    public void despawn() {
        mc.world.removeEntity(mc.world.getEntityByID(Integer.MAX_VALUE));
    }

}
