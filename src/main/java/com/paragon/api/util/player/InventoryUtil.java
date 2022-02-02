package com.paragon.api.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isHolding(Item item) {
        return mc.player.getHeldItemMainhand().getItem().equals(item) || mc.player.getHeldItemOffhand().getItem().equals(item);
    }

    public static boolean hasItem(Item item) {
        for (int i = 0; i < 36; i++) {
            Item itemInInv = mc.player.inventory.getStackInSlot(i).getItem();
            if (itemInInv == item) {
                return true;
            }
        }

        return false;
    }

    public static int getItemSlot(Item itemIn) {
        for (int i = 0; i < 36; i++) {
            Item itemInInv = mc.player.inventory.getStackInSlot(i).getItem();
            if (itemInInv == itemIn) {
                return i;
            }
        }

        return -1;
    }

    public static int getItemInHotbar(Item itemIn) {
        for(int i = 0; i < 9; i++) {
            Item itemInInv = mc.player.inventory.getStackInSlot(i).getItem();

            if(itemInInv == itemIn)
                return i;
        }

        return -1;
    }

    /**
     * Switches to an item in the player's hotbar
     * @param itemIn The item to switch to
     * @param silent Switch silently - use packets instead
     * @return Whether the switch was successful
     */
    public static boolean switchToItem(Item itemIn, boolean silent) {
        if(getItemInHotbar(itemIn) == -1) return false;

        if(silent) {
            mc.getConnection().sendPacket(new CPacketHeldItemChange(getItemInHotbar(itemIn)));
        } else {
            mc.player.inventory.currentItem = getItemInHotbar(itemIn);
        }

        return true;
    }
}
