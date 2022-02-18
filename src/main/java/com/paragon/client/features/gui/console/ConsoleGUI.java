package com.paragon.client.features.gui.console;

import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.Paragon;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class ConsoleGUI extends GuiScreen {

    @Override
    public void initGui() {
        Paragon.console.init();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Make the background darker
        if (GUI.darkenBackground.isEnabled()) {
            drawDefaultBackground();
        }

        // Render a cool gradient
        if (GUI.gradient.isEnabled()) {
            RenderUtil.drawGradientRect(0, height - 300, width, 300, 0, Colours.mainColour.getColour().getRGB());
        }

        Paragon.console.draw(mouseX, mouseY);

        Paragon.taskbar.drawTaskbar(mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Paragon.taskbar.mouseClicked(mouseX, mouseY);

        Paragon.console.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        Paragon.console.keyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return GUI.pause.isEnabled();
    }
}
