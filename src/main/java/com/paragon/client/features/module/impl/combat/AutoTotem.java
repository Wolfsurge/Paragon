package com.paragon.client.features.module.impl.combat;

import com.paragon.api.util.player.InventoryUtil;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoTotem extends Module {

    public AutoTotem() {
        super("AutoTotem", "Automatically puts totems in your offhand", Category.COMBAT);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (nullCheck()) {
            return;
        }

        doAutoTotem();
    }

    public void doAutoTotem() {
        if(InventoryUtil.hasItem(Items.TOTEM_OF_UNDYING)) {
            this.swapItems(getItemSlot(Items.TOTEM_OF_UNDYING));
        }
    }

    public void swapItems(int slot) {
        if (slot == -1 || mc.player.inventory.getStackInSlot(slot).getItem() != Items.TOTEM_OF_UNDYING) return;
        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }

    private int getItemSlot(Item input) {
        if (input == mc.player.getHeldItemOffhand().getItem()) return -1;
        int slot = 0;
        for(int i = 0; i < 36; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if(item == Items.TOTEM_OF_UNDYING)
                slot = i;
        }
        return slot;
    }

    private int getTotemCount() {
        int count = 0;
        for(int i = 0; i < 36; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if(item == Items.TOTEM_OF_UNDYING)
                count++;
        }
        return count;
    }

    @Override
    public String getModuleInfo() {
        return " " + getTotemCount();
    }

}
