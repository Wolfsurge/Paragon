package com.paragon.client.features.gui.panel.impl.module.sub;

import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.GuiUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.panel.impl.module.ModuleButton;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.settings.Setting;
import com.paragon.client.features.module.settings.impl.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author Wolfsurge
 */
public class SettingComponent implements TextRenderer {

    private ModuleButton moduleButton;
    private Setting setting;
    private float offset, height, subsettingHeight;

    private boolean expanded;
    private ArrayList<SettingComponent> settingComponents = new ArrayList<>();

    public SettingComponent(ModuleButton moduleButton, Setting setting, float offset, float height) {
        this.moduleButton = moduleButton;
        this.setting = setting;
        this.offset = offset;
        this.height = height;

        float settingOffset = getOffset() - 1;

        if (!setting.getSubsettings().isEmpty()) {
            for (Setting setting1 : setting.getSubsettings()) {
                if (setting1 instanceof BooleanSetting) {
                    getSettingComponents().add(new BooleanComponent(getModuleButton(), (BooleanSetting) setting1, settingOffset, 12));
                    settingOffset += 12;
                } else if (setting1 instanceof NumberSetting) {
                    getSettingComponents().add(new SliderComponent(getModuleButton(), (NumberSetting) setting1, settingOffset, 12));
                    settingOffset += 12;
                } else if (setting1 instanceof ModeSetting) {
                    getSettingComponents().add(new ModeComponent(getModuleButton(), (ModeSetting) setting1, settingOffset, 12));
                    settingOffset += 12;
                } else if (setting1 instanceof ColourSetting) {
                    getSettingComponents().add(new ColourComponent(getModuleButton(), (ColourSetting) setting1, settingOffset, 12));
                    settingOffset += 12;
                } else if (setting1 instanceof KeybindSetting) {
                    getSettingComponents().add(new KeybindComponent(getModuleButton(), (KeybindSetting) setting1, settingOffset, 12));
                    settingOffset += 12;
                }
            }
        }
    }

    public void renderSetting(int mouseX, int mouseY) {
        if (!getSettingComponents().isEmpty()) {
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            renderText("...", (getModuleButton().getPanel().getX() + getModuleButton().getPanel().getWidth() - 9) * 2, (getModuleButton().getOffset() + getOffset() + 3.5f) * 2, -1);
            GL11.glPopMatrix();
        }

        if (expanded) {
            getSettingComponents().forEach(settingComponent -> settingComponent.renderSetting(mouseX, mouseY));

            for (SettingComponent settingComponent : settingComponents) {
                RenderUtil.drawRect(getModuleButton().getPanel().getX(), getModuleButton().getOffset() + settingComponent.getOffset(), 2, settingComponent.getHeight(), Colours.mainColour.getColour().getRGB());
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1) {
            if (isMouseOver(mouseX, mouseY)) {
                expanded = !expanded;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return GuiUtil.mouseOver(getModuleButton().getPanel().getX(), getModuleButton().getOffset() + getOffset(), getModuleButton().getPanel().getX() + getModuleButton().getPanel().getWidth(), getModuleButton().getOffset() + getOffset() + getHeight(), mouseX, mouseY);
    }

    /**
     * Gets the module button
     * @return The module button
     */
    public ModuleButton getModuleButton() {
        return moduleButton;
    }

    /**
     * Gets the setting
     * @return The setting
     */
    public Setting getSetting() {
        return setting;
    }

    /**
     * Gets the offset
     * @return The offset
     */
    public float getOffset() {
        return offset;
    }

    /**
     * Sets the offset
     * @param newOffset The new offset
     */
    public void setOffset(float newOffset) {
        this.offset = newOffset;
    }

    /**
     * Gets whether the setting component is expanded or not
     * @return Whether the setting component is expanded or not
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * Gets the list of setting components
     * @return The setting components
     */
    public ArrayList<SettingComponent> getSettingComponents() {
        return settingComponents;
    }

    /**
     * Gets the height of the component (without subsettings)
     * @return The height of the component
     */
    public float getHeight() {
        return height;
    }

    /**
     * Gets the absolute height of the module
     * @return The absolute height of the module
     */
    public float getAbsoluteHeight() {
        return getHeight() + (expanded ? subsettingHeight : 0);
    }
}
