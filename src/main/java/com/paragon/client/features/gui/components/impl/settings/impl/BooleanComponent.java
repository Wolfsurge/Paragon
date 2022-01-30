package com.paragon.client.features.gui.components.impl.settings.impl;

import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.WindowGUI;
import com.paragon.client.features.gui.components.Window;
import com.paragon.client.features.gui.components.impl.ModuleButtonComponent;
import com.paragon.client.features.gui.components.impl.settings.SettingComponent;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import org.lwjgl.opengl.GL11;

/**
 * @author Wolfsurge
 * @since 30/01/22
 */
public class BooleanComponent extends SettingComponent implements TextRenderer {

    // Parent Window
    private Window parentWindow;

    // Parent Module Component
    private ModuleButtonComponent parentModuleButton;

    // Boolean Setting
    private final BooleanSetting booleanSetting;

    public BooleanComponent(ModuleButtonComponent parentModuleButton, Window parentWindow, BooleanSetting booleanSetting, float x, float y) {
        setParentWindow(parentWindow);
        setSetting(booleanSetting);
        this.booleanSetting = (BooleanSetting) getSetting();
        setX(x);
        setY(y);
        setWidth(193);
        setHeight(20);
    }

    /**
     * Renders the component
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     */
    @Override public void render(int mouseX, int mouseY) {
        RenderUtil.drawRect(getX(), getY(), getWidth(), getHeight(), WindowGUI.buttonColour);
        if(booleanSetting.isEnabled()) RenderUtil.drawRect(getX() - 1, getY(), 1, getHeight(), WindowGUI.mainColour);
        renderText(booleanSetting.getName(), getX() + 3, getY() + 3, -1);

        GL11.glPushMatrix();
        GL11.glScalef(.5f, .5f, 0); // Shrink scale
        renderText(booleanSetting.getDescription(), (getX() + 3) * 2, (getY() + 13) * 2, -1);
        GL11.glPopMatrix();
    }

    /**
     * Called when the mouse is clicked
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     * @param mouseButton The button that is clicked
     */
    @Override public void whenClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) booleanSetting.setEnabled(!booleanSetting.isEnabled());
    }

    /**
     * Gets the parent window
     * @return The parent window
     */
    public Window getParentWindow() {
        return parentWindow;
    }

    /**
     * Sets the parent window
     * @param parentWindow The new parent window
     */
    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    /**
     * Gets the parent module button
     * @return The parent module button
     */
    public ModuleButtonComponent getParentModuleButton() {
        return parentModuleButton;
    }

    /**
     * Sets the parent module button
     * @param parentModuleButton The new parent module button
     */
    public void setParentModuleButton(ModuleButtonComponent parentModuleButton) {
        this.parentModuleButton = parentModuleButton;
    }
}
