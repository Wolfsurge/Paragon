package com.paragon.client.features.gui.panel.impl.module;

import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.GuiUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.panel.impl.Panel;
import com.paragon.client.features.gui.panel.impl.module.sub.*;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import com.paragon.client.features.module.settings.Setting;
import com.paragon.client.features.module.settings.impl.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author Wolfsurge
 */
public class ModuleButton implements TextRenderer {

    // The parent panel
    private Panel panel;

    // The module
    private Module module;

    // The offset and height
    public float offset, height;

    // Whether the panel is expanded
    private boolean expanded = false;

    // A list of all setting components
    private ArrayList<SettingComponent> settingComponents = new ArrayList<>();

    public ModuleButton(Panel panel, Module module, float offset, float height) {
        this.panel = panel;
        this.module = module;
        this.offset = offset;
        this.height = height;

        float settingOffset = 13;

        for (Setting setting : getModule().getSettings()) {
            if (setting instanceof BooleanSetting) {
                settingComponents.add(new BooleanComponent(this, (BooleanSetting) setting, 13 + settingOffset, 12));
                settingOffset += 12;
            } else if (setting instanceof NumberSetting) {
                settingComponents.add(new SliderComponent(this, (NumberSetting) setting, settingOffset, 12));
                settingOffset += 12;
            } else if (setting instanceof ModeSetting) {
                settingComponents.add(new ModeComponent(this, (ModeSetting) setting, 13 + settingOffset, 12));
                settingOffset += 12;
            } else if (setting instanceof ColourSetting) {
                settingComponents.add(new ColourComponent(this, (ColourSetting) setting, 13 + settingOffset, 12));
                settingOffset += 12;
            } else if (setting instanceof KeybindSetting) {
                settingComponents.add(new KeybindComponent(this, (KeybindSetting) setting, 13 + settingOffset, 12));
                settingOffset += 12;
            }
        }
    }

    public void renderModuleButton(int mouseX, int mouseY) {
        // Header
        RenderUtil.drawRect(getPanel().getX(), getOffset(), getPanel().getWidth(), getHeight(), isMouseOver(mouseX, mouseY) ? GUI.buttonColour.getColour().brighter().getRGB() : GUI.buttonColour.getColour().getRGB());

        GL11.glPushMatrix();
        GL11.glScalef(0.8f, 0.8f, 0.8f);

        renderText(getModule().getName(), (getPanel().getX() + 3) * 1.25f, (getOffset() + 3.5f) * 1.25f, getModule().isEnabled() ? Colours.mainColour.getColour().getRGB() : -1);

        if (module.getSettings().size() > 1) {
            renderText("...", (getPanel().getX() + getPanel().getWidth() - 10) * 1.25f, (getOffset() + 2f) * 1.25f, -1);
        }

        GL11.glPopMatrix();

        if (getModule().isEnabled()) {
            RenderUtil.drawRect(getPanel().getX(), getOffset(), 1, getHeight(), Colours.mainColour.getColour().getRGB());
        }

        refreshSettingOffsets();

        if (expanded) {
            settingComponents.forEach(settingComponent -> {
                settingComponent.renderSetting(mouseX, mouseY);
            });

            for (SettingComponent settingComponent : settingComponents) {
                RenderUtil.drawRect(getPanel().getX(), getOffset() + settingComponent.getOffset(), 1, settingComponent.getHeight(), Colours.mainColour.getColour().getRGB());
            }
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return GuiUtil.mouseOver(getPanel().getX(), getOffset(), getPanel().getX() + getPanel().getWidth(), getOffset() + getHeight(), mouseX, mouseY);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOver(mouseX, mouseY)) {
                getModule().toggle();
            }
        } else if (mouseButton == 1) {
            if (isMouseOver(mouseX, mouseY)) {
                expanded = !expanded;
            }
        }

        if (expanded) {
            for (SettingComponent settingComponent : settingComponents) {
                settingComponent.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    public void keyTyped(char keyTyped, int keyCode) {

    }

    public void refreshSettingOffsets() {
        float settingOffset = 13;

        for (SettingComponent settingComponent : settingComponents) {
            settingComponent.setOffset(settingOffset);
            settingOffset += settingComponent.getHeight();

            if (settingComponent.isExpanded()) {
                for (SettingComponent settingComponent1 : settingComponent.getSettingComponents()) {
                    settingComponent1.setOffset(settingOffset);
                    settingOffset += settingComponent1.getHeight();
                }
            }
        }
    }

    /**
     * Gets the parent panel
     * @return The parent panel
     */
    public Panel getPanel() {
        return panel;
    }

    /**
     * Gets the module
     * @return The module
     */
    public Module getModule() {
        return module;
    }

    /**
     * Gets the offset
     * @return The offset
     */
    public float getOffset() {
        return offset;
    }

    /**
     * Gets the height of the button
     * @return The height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Gets the height of the button and it's settings
     * @return The absolute height
     */
    public float getAbsoluteHeight() {
        float settingHeight = 0;

        for (SettingComponent settingComponent : settingComponents) {
            settingHeight += settingComponent.getHeight();

            if (settingComponent.isExpanded()) {
                for (SettingComponent settingComponent1 : settingComponent.getSettingComponents()) {
                    settingHeight += settingComponent1.getHeight();
                }
            }
        }

        return expanded ? height + settingHeight : height;
    }
}