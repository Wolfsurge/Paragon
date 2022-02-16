package com.paragon.client.features.gui.panel.impl;

import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.GuiUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.Paragon;
import com.paragon.client.features.gui.panel.impl.module.ModuleButton;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.impl.other.Colours;

import java.util.ArrayList;

/**
 * @author Wolfsurge
 */
public class Panel implements TextRenderer {

    // X, Y, Width, and Bar Height
    private float x, y, width, barHeight;

    // The panel's category
    private Category category;

    // List of module buttons
    private ArrayList<ModuleButton> moduleButtons = new ArrayList<>();

    // Variables
    private boolean open = true;
    private boolean dragging = false;
    private float lastX, lastY;

    public Panel(float x, float y, float width, float barHeight, Category category) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.barHeight = barHeight;
        this.category = category;

        float offset = getY() + barHeight;

        // Add a new module button for each module in the category
        for (Module module : Paragon.moduleManager.getModulesInCategory(getCategory())) {
            moduleButtons.add(new ModuleButton(this, module, offset, 13));

            // Increase offset
            offset += 13;
        }
    }

    public void renderPanel(int mouseX, int mouseY) {
        // Set X and Y
        if (dragging) {
            this.x = mouseX - lastX;
            this.y = mouseY - lastY;
        }

        // Header
        RenderUtil.drawRect(getX(), getY(), getWidth(), 13, Colours.mainColour.getColour().getRGB());
        renderText(getCategory().getName(), getX() + 5, getY() + 3, -1);

        if (open) {
            refreshOffsets();

            // Draw modules
            moduleButtons.forEach(moduleButton -> {
                moduleButton.renderModuleButton(mouseX, mouseY);
            });
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        // Drag the frame if we have clicked on the header
        if (isMouseOverHeader(mouseX, mouseY) && mouseButton == 0) {
            this.lastX = mouseX - getX();
            this.lastY = mouseY - getY();

            dragging = true;
        }

        // Toggle the open state if we right-click on the header
        if (isMouseOverHeader(mouseX, mouseY) && mouseButton == 1) {
            open = !open;
        }

        // Call the mouseClicked event for each module button if the panel is open
        if (open) {
            moduleButtons.forEach(moduleButton -> {
                moduleButton.mouseClicked(mouseX, mouseY, mouseButton);
            });
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        // Make sure we aren't dragging
        dragging = false;

        // Mouse released.
        moduleButtons.forEach(moduleButton -> {
            moduleButton.mouseReleased(mouseX, mouseY, mouseButton);
        });
    }

    public void keyTyped(char keyTyped, int keyCode) {
        if (open) {
            moduleButtons.forEach(moduleButton -> {
                moduleButton.keyTyped(keyTyped, keyCode);
            });
        }
    }

    public boolean isMouseOverHeader(int mouseX, int mouseY) {
        return GuiUtil.mouseOver(getX(), getY(), getX() + getWidth(), getY() + barHeight, mouseX, mouseY);
    }

    /**
     * Sets all the module offsets
     */
    public void refreshOffsets() {
        float y = getY() + barHeight;

        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.offset = y;
            y += moduleButton.getAbsoluteHeight();
        }
    }

    /**
     * Gets the X
     * @return The X
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the Y
     * @return The Y
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the width
     * @return The width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the category
     * @return The category
     */
    public Category getCategory() {
        return category;
    }
}
