package com.paragon.asm.mixins.render.gui;

import com.paragon.client.Paragon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = GuiMainMenu.class, priority = Integer.MAX_VALUE)
public class MixinGuiMainMenu {

    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void renderWatermark(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Paragon " + TextFormatting.GRAY + Paragon.VERSION, 2, sr.getScaledHeight() - 50, new Color(128, 128, 255).getRGB());
    }

}
