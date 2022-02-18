package com.paragon.client.features.gui.panel;

import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.Paragon;
import com.paragon.client.features.gui.panel.impl.Panel;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import scala.xml.PrettyPrinter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Wolfsurge
 */
public class PanelGUI extends GuiScreen {

    // List of panels
    private ArrayList<Panel> panels = new ArrayList<>();

    public PanelGUI() {
        // X position of panel
        float x = 5;

        // Add a panel for every category
        for (Category category : Category.values()) {
            // Add panel
            panels.add(new Panel(x, 5, 90, 13, category));

            // Increase X
            x += 95;
        }
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

        scrollPanels();

        // Render panels
        panels.forEach(panel -> {
            panel.renderPanel(mouseX, mouseY);
        });

        Paragon.taskbar.drawTaskbar(mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        // Clicks
        panels.forEach(panel -> {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        });

        Paragon.taskbar.mouseClicked(mouseX, mouseY);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {

        // Click releases
        panels.forEach(panel -> {
            panel.mouseReleased(mouseX, mouseY, state);
        });

        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        // Keys typed
        panels.forEach(panel -> {
            panel.keyTyped(typedChar, keyCode);
        });

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        for (Module module : Paragon.moduleManager.modules) {
            Paragon.storageManager.saveModuleConfiguration(module);
        }

        Paragon.storageManager.saveSocial();
    }

    public void scrollPanels() {
        int dWheel = Mouse.getDWheel();

        for (Panel panel : panels) {
            if (dWheel > 0) {
                panel.setY(panel.getY() - GUI.scrollSpeed.getValue());
            } else if (dWheel < 0) {
                panel.setY(panel.getY() + GUI.scrollSpeed.getValue());
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        // Pause the game if pause is enabled in the GUI settings
        return GUI.pause.isEnabled();
    }

    /**
     * Gets the panels
     * @return The panels
     */
    public ArrayList<Panel> getPanels() {
        return panels;
    }
}
