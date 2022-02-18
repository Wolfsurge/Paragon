package com.paragon.client.features.gui.taskbar.icons;

import com.paragon.api.util.Wrapper;
import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.ColourUtil;
import com.paragon.api.util.render.GuiUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.module.impl.other.GUI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class Icon implements Wrapper, TextRenderer {

    private String name;
    private int x, y;
    private ResourceLocation location;
    private Supplier<GuiScreen> guiScreenSupplier;

    public Icon(String name, String iconFileName, int x, Supplier<GuiScreen> whenClicked) {
        this.name = name;
        this.x = x;
        this.location = new ResourceLocation("paragon", "textures/" + iconFileName + ".png");
        this.guiScreenSupplier = whenClicked;
    }

    public void draw(int mouseX, int mouseY) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        this.y = scaledResolution.getScaledHeight() - 40;

        ColourUtil.setColour(-1);

        if (GuiUtil.mouseOver(x, y, x + 32, y + 32, mouseX, mouseY)) {
            RenderUtil.drawRect(x, y + 1, 32, 30, GUI.buttonColour.getColour().getRGB());
            renderText(name, x + 16 - (getStringWidth(name) / 2f), y - 8.5f, -1);
        }

        GlStateManager.disableDepth();

        mc.getTextureManager().bindTexture(location);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 32, 32, 32, 32);
    }

    public void whenClicked(int mouseX, int mouseY) {
        if (GuiUtil.mouseOver(x, y, x + 32, y + 32, mouseX, mouseY)) {
            mc.displayGuiScreen(guiScreenSupplier.get());
        }
    }

}
