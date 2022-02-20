package com.paragon.client.features.gui.window.components.impl.settings;

import com.paragon.api.util.render.GuiUtil;
import com.paragon.client.features.gui.window.components.impl.settings.impl.*;
import com.paragon.client.features.module.settings.Setting;
import com.paragon.client.features.module.settings.impl.*;

import java.util.ArrayList;
import java.util.List;

public class SettingComponent {
    // Setting
    private Setting setting;

    // X, Y, Width, and Height
    private float x, y, width, height, settingHeight = 0;

    // List of subsetting components
    public final List<SettingComponent> settingComponents = new ArrayList<>();

    // Is the component expanded
    public boolean expanded = false;

    public void render(int mouseX, int mouseY) {}
    public void whenClicked(int mouseX, int mouseY, int mouseButton) {}
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}
    public void keyTyped(char typedChar, int keyCode) {}

    // Get the extra height
    public float getSettingHeight() {
        float settingHeight = 0f;

        for (SettingComponent settingComponent : settingComponents) {
            if (settingComponent.getSetting().isVisible()) {
                settingHeight += settingComponent.getHeight() + 0.5f;
            }
        }

        return expanded ? settingHeight : 0;
    }

    // Refresh subsetting offsets
    public void refreshOffsets() {
        float offset = getY() + getHeight() + 0.5f;

        for (SettingComponent settingComponent : settingComponents) {
            if (settingComponent.getSetting().isVisible()) {
                settingComponent.setY(offset);
                offset += settingComponent.getHeight() + 0.5f;
            }
        }
    }

    public boolean hasVisibleSubsettings() {
        for (SettingComponent settingComponent : settingComponents) {
            if (settingComponent.getSetting().isVisible()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether the mouse is over the button
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     * @return Whether the mouse is over the button
     */
    public boolean isMouseOnButton(int mouseX, int mouseY) {
        return GuiUtil.mouseOver(getX(), getY(), getX() + getWidth(), getY() + getHeight(), mouseX, mouseY);
    }

    /**
     * Gets the X of the component
     * @return The X of the component
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the X of the component
     * @param x The new X
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets the Y of the component
     * @return The Y of the component
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the Y of the component
     * @param y The new Y of the component
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets the width of the component
     * @return The width of the component
     */
    public float getWidth() {
        return width;
    }

    /**
     * Sets the width of the component
     * @param width The new width of the component
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Gets the height of the component
     * @return The height of the component
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the height of the component
     * @param height The new height of the component
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Gets the setting
     * @return The setting
     */
    public Setting getSetting() {
        return setting;
    }

    /**
     * Sets the setting
     * @param setting The new setting
     */
    public void setSetting(Setting setting) {
        this.setting = setting;
    }
}