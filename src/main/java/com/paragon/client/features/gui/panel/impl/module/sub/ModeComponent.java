package com.paragon.client.features.gui.panel.impl.module.sub;

import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.panel.impl.module.ModuleButton;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModeComponent extends SettingComponent {

    public ModeComponent(ModuleButton moduleButton, ModeSetting setting, float offset, float height) {
        super(moduleButton, setting, offset, height);
    }

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        RenderUtil.drawRect(getModuleButton().getPanel().getX(), getModuleButton().getOffset() + getOffset(), getModuleButton().getPanel().getWidth(), getHeight(), isMouseOver(mouseX, mouseY) ? GUI.buttonColour.getColour().brighter().getRGB() : GUI.buttonColour.getColour().getRGB());

        String mode = ((ModeSetting) getSetting()).getCurrentMode();
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        renderText(getSetting().getName() + TextFormatting.GRAY + " " + mode, (getModuleButton().getPanel().getX() + 4) * 2, (getModuleButton().getOffset() + getOffset() + 4.5f) * 2, -1);
        GL11.glPopMatrix();

        super.renderSetting(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOver(mouseX, mouseY)) {
                // Cycle mode
                ((ModeSetting) getSetting()).cycleMode();
            }
        }

        if (isExpanded()) {
            getSettingComponents().forEach(settingComponent -> {
                if (settingComponent.getSetting().isVisible()) {
                    settingComponent.mouseClicked(mouseX, mouseY, mouseButton);
                }
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
