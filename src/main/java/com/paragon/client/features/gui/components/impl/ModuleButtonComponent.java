package com.paragon.client.features.gui.components.impl;

import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.GuiUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.Paragon;
import com.paragon.client.features.gui.WindowGUI;
import com.paragon.client.features.gui.components.impl.settings.SettingComponent;
import com.paragon.client.features.gui.components.impl.settings.impl.*;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import com.paragon.client.features.module.settings.Setting;
import com.paragon.client.features.module.settings.impl.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import scala.xml.PrettyPrinter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wolfsurge
 * @since 30/01/22
 */

public class ModuleButtonComponent implements TextRenderer {

    // The module
    private Module module;

    // The parent category
    private CategoryComponent parentCategory;

    // The X, Y, Width, and Height of the button
    private float x, y, width, height;

    // All the settings
    private final List<SettingComponent> settingComponents = new ArrayList<>();

    public ModuleButtonComponent(Module module, CategoryComponent parentCategory, float x, float y) {
        setModule(module);
        setParentCategory(parentCategory);
        setX(x);
        setY(y);
        setWidth(193);
        setHeight(20);

        // Add the settings
        float yOffset = getParentCategory().getY() + 20;
        for(Setting s : getModule().getSettings()) {
            SettingComponent settingComponent = null;
            if(s instanceof BooleanSetting) settingComponent = new BooleanComponent(this, getParentCategory().getParentWindow(), (BooleanSetting) s, getParentCategory().getParentWindow().getX() + 204, yOffset);
            else if(s instanceof ModeSetting) settingComponent = new ModeComponent(this, getParentCategory().getParentWindow(), (ModeSetting) s, getParentCategory().getParentWindow().getX() + 204, yOffset);
            else if(s instanceof NumberSetting) settingComponent = new SliderComponent(this, getParentCategory().getParentWindow(), (NumberSetting) s, getParentCategory().getParentWindow().getX() + 204, yOffset);
            else if(s instanceof KeybindSetting) settingComponent = new KeybindComponent(this, getParentCategory().getParentWindow(), (KeybindSetting) s, getParentCategory().getParentWindow().getX() + 204, yOffset);
            else if(s instanceof ColourSetting) settingComponent = new ColourComponent(this, getParentCategory().getParentWindow(), (ColourSetting) s, getParentCategory().getParentWindow().getX() + 204, yOffset);
            else continue;

            getSettingComponents().add(settingComponent);
            
            yOffset += settingComponent.getHeight();
        }
    }

    /**
     * Renders the module button
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     */
    public void render(int mouseX, int mouseY) {
        RenderUtil.drawRect(getX(), getY(), getWidth(), getHeight(), GUI.buttonColour.getColour().getRGB());

        // Module outline
        if(getModule().isEnabled() || getParentCategory().getSelectedModule() == getModule() && GUI.settingOutline.isEnabled())
            RenderUtil.drawRect(getX() - 1, getY(), 1, getHeight(), Colours.mainColour.getColour().getRGB());

        renderText(getModule().getName(), getX() + 3, getY() + 3, getModule().isEnabled() ? Colours.mainColour.getColour().getRGB() : -1);

        GL11.glPushMatrix();
        GL11.glScalef(.5f, .5f, 0); // Shrink scale
        renderText(getModule().getDescription(), (getX() + 3) * 2, (getY() + 13) * 2, -1);
        GL11.glPopMatrix();

        renderText("Visible", getX() + getWidth() - 40, getY() + 6, getModule().isVisible() ? Colours.mainColour.getColour().getRGB() : -1);

        if(getParentCategory().getSelectedModule() == getModule()) {
            RenderUtil.startGlScissor(getParentCategory().getParentWindow().getX() + 203, getParentCategory().getY() + 21, 197, 258);
            for(SettingComponent settingComponent : settingComponents) {
                settingComponent.render(mouseX, mouseY);
            }
            RenderUtil.endGlScissor();
        }

        // Setting Outline
        if(getParentCategory().getSelectedModule() == getModule() && GUI.settingOutline.isEnabled()) {
            // Module
            RenderUtil.drawRect(getX(), getY(), getWidth() + 4, 1, Colours.mainColour.getColour().getRGB());
            RenderUtil.drawRect(getX(), getY() + getHeight() - 1, getWidth() + 4, 1, Colours.mainColour.getColour().getRGB());

            // Settings
            RenderUtil.drawRect(getX() + getWidth() + 4, parentCategory.getY() + 18, 1, 263, Colours.mainColour.getColour().getRGB());
            RenderUtil.drawRect(getX() + getWidth() + 4, parentCategory.getY() + 18 + 263, 197, 1, Colours.mainColour.getColour().getRGB());
            RenderUtil.drawRect(getX() + getWidth() + 201, parentCategory.getY() + 18, 1, 264, Colours.mainColour.getColour().getRGB());
            RenderUtil.drawRect(getX() + getWidth() + 4, parentCategory.getY() + 18, 197, 1, Colours.mainColour.getColour().getRGB());

            // Space
            RenderUtil.drawRect(getX() + getWidth() + 4, getY() + 1, 1, getHeight() - 2, GUI.buttonColour.getColour().getRGB());
        } else if(getParentCategory().getSelectedModule() == getModule()) {
            RenderUtil.drawRect(getX() + getWidth() - 1, getY(), 1, getHeight(), Colours.mainColour.getColour().getRGB());
        }

        settingMouseScroll(mouseX, mouseY);
    }

    /**
     * Called when the mouse is clicked
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseClicked) {
        if(isMouseOnButton(mouseX, mouseY)) {
            // toggle module
            if (mouseClicked == 0 && GuiUtil.mouseOver(getX(), getY(), getX() + getWidth() - 41, getY() + getHeight(), mouseX, mouseY))
                getModule().toggle();
                // toggle module visibility
            else if (mouseClicked == 0 && GuiUtil.mouseOver(getX() + getWidth() - 40, getY(), getX() + getWidth(), getY() + getHeight(), mouseX, mouseY))
                getModule().setVisible(!getModule().isVisible());
                // set selected module to this
            else if (mouseClicked == 1) {
                getParentCategory().setSelectedModule(getModule());
            }
        } else if(isMouseOverSettings(mouseX, mouseY) && getParentCategory().getSelectedModule() == getModule()) {
            for(SettingComponent settingComponent : settingComponents)
                if(settingComponent.isMouseOnButton(mouseX, mouseY)) settingComponent.whenClicked(mouseX, mouseY, mouseClicked);
        }
    }


    /**
     * Triggered when the mouse is released
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     * @param mouseButton The mouse button that is released
     */
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for(SettingComponent settingComponent : getSettingComponents())
            settingComponent.mouseReleased(mouseX, mouseY, mouseButton);
    }

    /**
     * Checks if the mouse is over the settings compartment
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     * @return Is the mouse over the settings compartment
     */
    public boolean isMouseOverSettings(int mouseX, int mouseY) {
        return GuiUtil.mouseOver(getParentCategory().getParentWindow().getX() + 203, getParentCategory().getY() + 20, getParentCategory().getParentWindow().getX() + 400, getParentCategory().getY() + 283, mouseX, mouseY);
    }

    public void settingMouseScroll(int mouseX, int mouseY) {
        SettingComponent firstSettingComponent = getSettingComponents().get(0);
        SettingComponent lastSettingComponent = getSettingComponents().get(getSettingComponents().size() - 1);

        int mouseWheel = Mouse.getDWheel();
        // Setting Scrolling
        if(isMouseOverSettings(mouseX, mouseY)) {
            if(mouseWheel < 0) {
                float settingEndY = (getParentCategory().getParentWindow().getY() + 35 + 263) - lastSettingComponent.getHeight();
                if (lastSettingComponent.getY() > settingEndY)
                    for(SettingComponent settingComponent : getSettingComponents())
                        settingComponent.setY(settingComponent.getY() - 10);
            } else if(mouseWheel > 0) {
                if (firstSettingComponent.getY() < getParentCategory().getY() + 20)
                    for(SettingComponent settingComponent : getSettingComponents())
                        settingComponent.setY(settingComponent.getY() + 10);
            }
        }
    }

    /**
     * Triggered when a key is pressed
     * @param typedChar The character typed
     * @param keyCode The key code of the character
     */
    public void keyTyped(char typedChar, int keyCode) {
        for(SettingComponent settingComponent : getSettingComponents())
            settingComponent.keyTyped(typedChar, keyCode);
    }

    public boolean isMouseOnButton(int mouseX, int mouseY) {
        return GuiUtil.mouseOver(getX(), getY(), getX() + getWidth(), getY() + getHeight(), mouseX, mouseY);
    }

    /**
     * Gets the module
     * @return The module
     */
    public Module getModule() {
        return module;
    }

    /**
     * Sets the module
     * @param module The module to be set as
     */
    public void setModule(Module module) {
        this.module = module;
    }

    /**
     * Gets the parent category button
     * @return The parent category button
     */
    public CategoryComponent getParentCategory() {
        return parentCategory;
    }

    /**
     * Sets the parent category
     * @param parentCategory The new parent category
     */
    public void setParentCategory(CategoryComponent parentCategory) {
        this.parentCategory = parentCategory;
    }

    /**
     * Gets the X of the button
     * @return The X of the button
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the X of the button
     * @param x The X of the button
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets the Y of the button
     * @return The Y of the button
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the Y of the button
     * @param y The Y of the button
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets the width of the button
     * @return The width of the button
     */
    public float getWidth() {
        return width;
    }

    /**
     * Sets the width of the button
     * @param width The new width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Gets the height of the button
     * @return The height of the button
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the height of the button
     * @param height The new height of the button
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Gets the setting components
     * @return The setting components
     */
    public List<SettingComponent> getSettingComponents() {
        return settingComponents;
    }

}
