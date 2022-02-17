package com.paragon.api.util.player;

import com.mojang.authlib.GameProfile;
import com.paragon.api.util.Wrapper;
import net.minecraft.client.entity.EntityOtherPlayerMP;

/**
 * @author Wolfsurge
 */
public class EntityFakePlayer extends EntityOtherPlayerMP implements Wrapper {

    public EntityFakePlayer(String name) {
        super(mc.world, new GameProfile(mc.player.getGameProfile().getId(), name));
        copyLocationAndAnglesFrom(mc.player);
        this.inventory.copyInventory((mc.player).inventory);
        this.rotationYawHead = (mc.player).rotationYawHead;
        this.renderYawOffset = (mc.player).renderYawOffset;
        this.chasingPosX = this.posX;
        this.chasingPosY = this.posY;
        this.chasingPosZ = this.posZ;
        mc.world.addEntityToWorld(-500, this);
    }

    public void despawn() {
        mc.world.removeEntityFromWorld(-500);
    }

}
