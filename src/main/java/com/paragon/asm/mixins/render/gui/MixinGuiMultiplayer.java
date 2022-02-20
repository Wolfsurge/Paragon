package com.paragon.asm.mixins.render.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer extends GuiScreen {

    @Inject(method = "initGui", at = @At("TAIL"))
    public void initGui(CallbackInfo ci) {
        // this.buttonList.add(new GuiButton(-5, 5, 5, 75, 20, "Alts"));
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    public void actionPerformed(GuiButton guiButton, CallbackInfo ci) {

    }

}
