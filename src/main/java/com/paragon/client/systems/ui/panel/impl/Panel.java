package com.paragon.client.systems.ui.panel.impl;

import com.paragon.Paragon;
import com.paragon.api.util.render.GuiUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.api.util.render.TextRenderer;
import com.paragon.client.systems.module.impl.client.ClientFont;
import com.paragon.client.systems.ui.animation.Animation;
import com.paragon.client.systems.ui.panel.impl.module.ModuleButton;
import com.paragon.client.systems.module.Module;
import com.paragon.client.systems.module.ModuleCategory;
import com.paragon.client.systems.module.impl.client.Colours;
import com.paragon.client.systems.module.impl.client.GUI;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Wolfsurge
 */
public class Panel implements TextRenderer {

    // X, Y, Width, and Bar Height
    private float x, y, width, barHeight;

    // The panel's category
    private ModuleCategory category;

    // List of module buttons
    private ArrayList<ModuleButton> moduleButtons = new ArrayList<>();

    // Opening / Closing animation
    private Animation animation;

    // Variables
    private boolean dragging = false;
    private float lastX, lastY;

    public Panel(float x, float y, float width, float barHeight, ModuleCategory category) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.barHeight = barHeight;
        this.category = category;

        float offset = getY() + barHeight;

        // Add a new module button for each module in the category
        for (Module module : Paragon.INSTANCE.getModuleManager().getModulesInCategory(getCategory())) {
            moduleButtons.add(new ModuleButton(this, module, offset, 12));

            // Increase offset
            offset += 12;
        }

        animation = new Animation(300, true);
    }

    public void renderPanel(int mouseX, int mouseY) {
        // Set animation speed
        animation.time = GUI.animationSpeed.getValue();

        // Set X and Y
        if (dragging) {
            this.x = mouseX - lastX;
            this.y = mouseY - lastY;
        }

        // Header
        RenderUtil.drawRect(getX(), getY(), getWidth(), barHeight, new Color(23, 23, 23).darker().getRGB());
        renderCenteredString(getCategory().getName(), getX() + (getWidth() / 2f), getY() + (barHeight / 2f) + (ClientFont.INSTANCE.isEnabled() ? 0 : 0.5f), -1, true);

        float height = 0;
        for (ModuleButton moduleButton : moduleButtons) {
            height += moduleButton.getAbsoluteHeight();
        }

        refreshOffsets();

        RenderUtil.startGlScissor(getX() - 0.5f, getY(), getWidth() + 1, barHeight + (height * animation.getAnimationFactor()) + 0.5f);

        if (isExpanded()) {
            // Draw modules
            moduleButtons.forEach(moduleButton -> {
                moduleButton.renderModuleButton(mouseX, mouseY);
            });
        }

        RenderUtil.endGlScissor();

        RenderUtil.drawRect(getX(), getY() + barHeight + (height * animation.getAnimationFactor()), getWidth(), 3, new Color(23, 23, 23).darker().getRGB());

        if (GUI.panelHeaderSeparator.isEnabled()) {
            RenderUtil.drawRect(getX(), getY() + barHeight, getWidth(), 0.5f, Colours.mainColour.getColour().getRGB());
        }

        if (GUI.outline.isEnabled()) {
            RenderUtil.drawBorder(getX(), getY(), getWidth(), barHeight + (3 * animation.getAnimationFactor()) + height * animation.getAnimationFactor(), 0.5f, Colours.mainColour.getColour().getRGB());
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
            animation.setState(!isExpanded());
        }

        // Call the mouseClicked event for each module button if the panel is open
        if (isExpanded()) {
            moduleButtons.forEach(moduleButton -> {
                moduleButton.mouseClicked(mouseX, mouseY, mouseButton);
            });
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            // Make sure we aren't dragging
            dragging = false;
        }

        // Mouse released.
        moduleButtons.forEach(moduleButton -> {
            moduleButton.mouseReleased(mouseX, mouseY, mouseButton);
        });
    }

    public void keyTyped(char keyTyped, int keyCode) {
        if (isExpanded()) {
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
            y += moduleButton.getAbsoluteHeight() * GUI.animation.getCurrentMode().getAnimationFactor(animation.getAnimationFactor());
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
     * Sets the Y
     * @param newY The new Y
     */
    public void setY(float newY) {
        this.y = newY;
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
    public ModuleCategory getCategory() {
        return category;
    }

    /**
     * Gets whether the animation is expanding / expanded
     * @return If the panel is expanded
     */
    public boolean isExpanded() {
        return animation.getAnimationFactor() > 0;
    }
}