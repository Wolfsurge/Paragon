package com.paragon.api.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;

public class InventoryUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isHolding(Item item) {
        return mc.player.getHeldItemMainhand().getItem().equals(item) || mc.player.getHeldItemOffhand().getItem().equals(item);
    }

    public static boolean hasItem(Item input) {
        for (int i = 0; i < 36; i++) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                return true;
            }
        }

        return false;
    }
}
