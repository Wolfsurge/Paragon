package com.paragon.client.features.gui.window;

import com.paragon.client.Paragon;
import com.paragon.client.features.gui.window.components.Window;
import com.paragon.client.features.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

/**
 * @author Wolfsurge
 * @since 29/01/22
 */
public class WindowGUI extends GuiScreen {

    // The window to be rendered
    private final Window mainWindow;

    public WindowGUI() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        // Create window
        mainWindow = new Window("Paragon " + Paragon.VERSION, scaledResolution.getScaledWidth() / 2f - 200, scaledResolution.getScaledHeight() / 2f - 150, 400, 300);

        for(Module m : Paragon.moduleManager.modules) {
            Paragon.storageManager.loadModuleConfiguration(m);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Make the background darker
        drawDefaultBackground();

        // Render window
        mainWindow.render(mouseX, mouseY);
    }

    /**
     * Called when the GUI is closed
     */
    @Override
    public void onGuiClosed() {
        for(Module module : Paragon.moduleManager.modules)
            Paragon.storageManager.saveModuleConfiguration(module);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // Handle clicks in the window
        mainWindow.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        mainWindow.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        mainWindow.keyTyped(typedChar, keyCode);
    }

    /**
     * Stops the game being paused when in the screen
     * @return Is the game paused whilst in the screen
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

}
