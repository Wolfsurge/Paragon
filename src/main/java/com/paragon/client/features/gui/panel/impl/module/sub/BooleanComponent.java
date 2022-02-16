package com.paragon.client.features.gui.panel.impl.module.sub;

import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.panel.impl.module.ModuleButton;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import org.lwjgl.opengl.GL11;

public class BooleanComponent extends SettingComponent {

    public BooleanComponent(ModuleButton moduleButton, BooleanSetting setting, float offset, float height) {
        super(moduleButton, setting, offset, height);
    }

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        // Background
        RenderUtil.drawRect(getModuleButton().getPanel().getX(), getModuleButton().getOffset() + getOffset(), getModuleButton().getPanel().getWidth(), getHeight(), isMouseOver(mouseX, mouseY) ? GUI.buttonColour.getColour().brighter().getRGB() : GUI.buttonColour.getColour().getRGB());

        // Render setting name
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        renderText(getSetting().getName(), (getModuleButton().getPanel().getX() + 4) * 2f, (getModuleButton().getOffset() + getOffset() + 4.5f) * 2f, ((BooleanSetting) getSetting()).isEnabled() ? Colours.mainColour.getColour().getRGB() : -1);
        GL11.glPopMatrix();

        super.renderSetting(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOver(mouseX, mouseY)) {
                // Toggle setting
                ((BooleanSetting) getSetting()).setEnabled(!((BooleanSetting) getSetting()).isEnabled());
            }
        }

        if (isExpanded()) {
            getSettingComponents().forEach(settingComponent -> {
                settingComponent.mouseClicked(mouseX, mouseY, mouseButton);
            });
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public float getAbsoluteHeight() {
        float subsettingHeight = 0;

        for (SettingComponent settingComponent : getSettingComponents()) {
            subsettingHeight += settingComponent.getHeight();
        }

        return isExpanded() ? getHeight() + subsettingHeight : getHeight();
    }
}
