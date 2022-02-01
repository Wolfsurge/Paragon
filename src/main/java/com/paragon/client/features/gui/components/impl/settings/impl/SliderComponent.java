package com.paragon.client.features.gui.components.impl.settings.impl;

import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.GuiUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.components.Window;
import com.paragon.client.features.gui.components.impl.ModuleButtonComponent;
import com.paragon.client.features.gui.components.impl.settings.SettingComponent;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Wolfsurge
 * @since 30/01/22
 */
public class SliderComponent extends SettingComponent implements TextRenderer {

    // Parent Window
    private Window parentWindow;

    // Parent Module Component
    private ModuleButtonComponent parentModuleButton;

    // Number Setting
    private final NumberSetting numberSetting;

    private boolean dragging;
    private float renderWidth;

    public SliderComponent(ModuleButtonComponent parentModuleButton, Window parentWindow, NumberSetting numberSetting, float x, float y) {
        setParentWindow(parentWindow);
        setSetting(numberSetting);
        this.numberSetting = (NumberSetting) getSetting();
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
        update(mouseX, mouseY);

        RenderUtil.drawRect(getX(), getY(), getWidth(), getHeight(), GUI.buttonColour.getColour().getRGB());
        renderText(numberSetting.getName() + TextFormatting.GRAY + " " + numberSetting.getValue() + " / " + numberSetting.getMax(), getX() + 3, getY() + 3, -1);

        GL11.glPushMatrix();
        GL11.glScalef(.5f, .5f, 0); // Shrink scale
        renderText(numberSetting.getDescription(), (getX() + 3) * 2, (getY() + 13) * 2, -1);
        GL11.glPopMatrix();

        RenderUtil.drawRect(getX(), getY() + getHeight() - 1, renderWidth, 1, Colours.mainColour.getColour().getRGB());
    }

    public void update(int mouseX, int mouseY) {
        float diff = Math.min(193, Math.max(0, mouseX - getX()));

        float min = numberSetting.getMin();
        float max = numberSetting.getMax();

        renderWidth = (193) * (numberSetting.getValue() - min) / (max - min);

        if (dragging) {
            if (diff == 0) {
                numberSetting.setValue(numberSetting.getMin());
            } else {
                float newValue = roundToPlace(((diff / 193) * (max - min) + min), 2);
                numberSetting.setValue(newValue);
            }
        }
    }

    /**
     * Rounds a value to the nearest place
     * @param value The value input
     * @param places How many places to round to
     * @return Rounded value
     */
    private static float roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    /**
     * Called when the mouse is clicked
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     * @param mouseButton The button that is clicked
     */
    @Override public void whenClicked(int mouseX, int mouseY, int mouseButton) {
        if(isMouseOnButton(mouseX, mouseY))
            dragging = true;
    }

    @Override public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        dragging = false;
    }

    public boolean isMouseOnButton(int mouseX, int mouseY) {
        return GuiUtil.mouseOver(getX(), getY(), getX() + getWidth(), getY() + getHeight(), mouseX, mouseY);
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
