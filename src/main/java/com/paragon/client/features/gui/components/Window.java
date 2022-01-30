package com.paragon.client.features.gui.components;

import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.WindowGUI;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.gui.components.impl.CategoryComponent;
import com.paragon.client.features.gui.components.impl.ModuleButtonComponent;
import com.paragon.client.features.gui.components.impl.settings.SettingComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wolfsurge
 * @since 29/01/22
 */
public class Window implements TextRenderer {

    // Window Title
    private String title;

    // X and Y of the window
    private float x, y;

    // Width and Height of the window
    private float width, height;

    // List of category buttons
    private List<CategoryComponent> categoryButtons = new ArrayList<>();

    // The current selected category
    private CategoryComponent selectedCategory;

    /**
     * Create new window
     * @param title Title of window which is displayed at the top
     * @param x Default X of window
     * @param y Default Y of window
     * @param width Width of window
     * @param height Height of window
     */
    public Window(String title, float x, float y, float width, float height) {
        setTitle(title);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

        // Add the category buttons
        float categoryX = getX() + 2;
        for(Category c : Category.values()) {
            CategoryComponent categoryComponent = new CategoryComponent(this, c, categoryX, getY() + 17);
            categoryButtons.add(categoryComponent);
            categoryX += categoryComponent.getWidth() + 2;
        }

        // Defaults the selected category to combat
        setSelectedCategory(categoryButtons.get(0));
    }

    /**
     * Render the window
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     */
    public void render(int mouseX, int mouseY) {
        // Main Background
        RenderUtil.drawRect(getX(), getY(), getWidth(), getHeight(), WindowGUI.backgroundColour);

        // Title
        RenderUtil.drawRect(getX(), getY() + 15, getWidth(), 1, WindowGUI.mainColour);
        renderText(getTitle(), getX() + 3, getY() + 3.5f, -1);

        // Setting Background
        RenderUtil.drawRect(getX() + 201, getY() + 35, 197, 263, WindowGUI.generalColour);

        // Render categories
        for(CategoryComponent categoryComponent : categoryButtons)
            categoryComponent.renderCategory(mouseX, mouseY);

        // Separator
        RenderUtil.drawRect(getX(), getY() + 32, getWidth(), 1, WindowGUI.mainColour);
    }

    /**
     * Called when the mouse is clicked
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     * @param mouseButton The button which is clicked
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for(CategoryComponent categoryComponent : categoryButtons) {
            if(categoryComponent.isMouseOnButton(mouseX, mouseY)) {
                categoryComponent.whenClicked();
            }
        }

        for(ModuleButtonComponent moduleButtonComponent : getSelectedCategory().getModuleButtons()) {
            if(moduleButtonComponent.isMouseOnButton(mouseX, mouseY)) {
                moduleButtonComponent.whenClicked(mouseX, mouseY,  mouseButton);
            }

            if(getSelectedCategory().getSelectedModule() == moduleButtonComponent.getModule()) {
                for(SettingComponent settingComponent : moduleButtonComponent.getSettingComponents()) {
                    if(settingComponent.isMouseOnButton(mouseX, mouseY)) {
                        settingComponent.whenClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }

    }

    /**
     * Triggered when the mouse is released
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     * @param mouseButton The mouse button that is released
     */
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for(ModuleButtonComponent moduleButtonComponent : getSelectedCategory().getModuleButtons())
            moduleButtonComponent.mouseReleased(mouseX, mouseY, mouseButton);
    }

    /**
     * Triggered when a key is pressed
     * @param typedChar The character typed
     * @param keyCode The key code of the character
     */
    public void keyTyped(char typedChar, int keyCode) {
        for(ModuleButtonComponent moduleButtonComponent : getSelectedCategory().getModuleButtons())
            moduleButtonComponent.keyTyped(typedChar, keyCode);
    }

    /**
     * Gets the title of the window
     * @return The title of the window
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the window
     * @param title The new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the X of the window
     * @return The X of the window
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the X of the window
     * @param x The new X
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets the Y of the window
     * @return The Y of the window
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the Y of the window
     * @param y The new Y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets the width of the window
     * @return The width of the window
     */
    public float getWidth() {
        return width;
    }

    /**
     * Sets the width of the window
     * @param width The width of the window
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Gets the height of the window
     * @return The height of the window
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the height of the window
     * @param height The new height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Gets the category buttons
     * @return The category buttons
     */
    public List<CategoryComponent> getCategoryButtons() {
        return categoryButtons;
    }

    /**
     * Gets the selected category
     * @return The selected category
     */
    public CategoryComponent getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * Sets the selected category
     * @param selectedCategory The new selected category
     */
    public void setSelectedCategory(CategoryComponent selectedCategory) {
        this.selectedCategory = selectedCategory;
    }
}
