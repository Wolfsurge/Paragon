package com.paragon.client.features.gui.components.impl;

import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.GuiUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.Paragon;
import com.paragon.client.features.gui.WindowGUI;
import com.paragon.client.features.gui.components.Window;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wolfsurge
 * @since 30/01/22
 */

public class CategoryComponent implements TextRenderer {

    // Category of the component
    public Category category;

    // x, y, width, and height
    private float x, y, width, height;

    // The parent window
    private Window parentWindow;

    // List of module buttons
    private final List<ModuleButtonComponent> moduleButtons;

    // Module to display the settings for
    private Module selectedModule;

    /**
     * Creates a new category component
     * @param category The category
     * @param x The x position
     * @param y The y position
     */
    public CategoryComponent(Window parentWindow, Category category, float x, float y) {
        setParentWindow(parentWindow);
        setCategory(category);
        setX(x);
        setY(y);
        setWidth(getStringWidth(getCategory().getName()) + 8);
        setHeight(14);

        moduleButtons = new ArrayList<>();

        // Add buttons
        float yOffset = getY() + 20;
        for(Module m : Paragon.moduleManager.getModulesInCategory(getCategory())) {
            ModuleButtonComponent moduleButtonComponent = new ModuleButtonComponent(m, this, getParentWindow().getX() + 4, yOffset);
            moduleButtons.add(moduleButtonComponent);
            yOffset += moduleButtonComponent.getHeight() + .5f;
        }
    }

    /**
     * Renders the category button
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     */
    public void renderCategory(int mouseX, int mouseY) {
        RenderUtil.drawRect(getX(), getY(), getWidth(), getHeight(), getParentWindow().getSelectedCategory() == this ? new Color(WindowGUI.buttonColour).brighter().getRGB() : WindowGUI.buttonColour);
        renderText(getCategory().getName(), getX() + 4, getY() + 3, -1);

        if(getParentWindow().getSelectedCategory() == this) {
            RenderUtil.drawRect(getParentWindow().getX() + 2, getParentWindow().getY() + 35, 197, 263, WindowGUI.generalColour);
            for (ModuleButtonComponent moduleButtonComponent : moduleButtons) moduleButtonComponent.render(mouseX, mouseY);
            RenderUtil.drawRect(getX(), getY() + getHeight() - 1, getWidth(), 1, WindowGUI.mainColour);
        }
    }

    /**
     * Called when the button is clicked
     */
    public void whenClicked() {
        // Sets the window's category to this component's category
        getParentWindow().setSelectedCategory(this);
    }

    public boolean isMouseOnButton(int mouseX, int mouseY) {
        return GuiUtil.mouseOver(getX(), getY(), getX() + getWidth(), getY() + getHeight(), mouseX, mouseY);
    }

    /**
     * Gets the category
     * @return The category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the category
     * @param category The new category
     */
    public void setCategory(Category category) {
        this.category = category;
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
     * @param y The new Y
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
     * @param height The new height
     */
    public void setHeight(float height) {
        this.height = height;
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
     * Gets the module buttons
     * @return The module buttons
     */
    public List<ModuleButtonComponent> getModuleButtons() {
        return moduleButtons;
    }

    /**
     * Gets the selected module
     * @return The selected module
     */
    public Module getSelectedModule() {
        return selectedModule;
    }

    /**
     * Sets the selected module
     * @param selectedModule The new selected module
     */
    public void setSelectedModule(Module selectedModule) {
        this.selectedModule = selectedModule;
    }
}
