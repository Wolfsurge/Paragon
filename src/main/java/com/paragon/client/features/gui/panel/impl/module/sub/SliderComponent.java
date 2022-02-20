package com.paragon.client.features.gui.panel.impl.module.sub;

import com.paragon.api.util.miscellaneous.MathUtils;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.panel.impl.module.ModuleButton;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import net.minecraft.util.text.TextComponentTranslationFormatException;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SliderComponent extends SettingComponent {

    private boolean dragging = false;

    public SliderComponent(ModuleButton moduleButton, NumberSetting setting, float offset, float height) {
        super(moduleButton, setting, offset, height);
    }

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        RenderUtil.drawRect(getModuleButton().getPanel().getX(), getModuleButton().getOffset() + getOffset(), getModuleButton().getPanel().getWidth(), getHeight(), isMouseOver(mouseX, mouseY) ? GUI.buttonColour.getColour().brighter().getRGB() : GUI.buttonColour.getColour().getRGB());

        float renderWidth;

        // Set values
        float diff = Math.min(84, Math.max(0, mouseX - (getModuleButton().getPanel().getX() + 6)));

        float min = ((NumberSetting) getSetting()).getMin();
        float max = ((NumberSetting) getSetting()).getMax();

        renderWidth = 84 * (((NumberSetting) getSetting()).getValue() - min) / (max - min);

        if (!Mouse.isButtonDown(0))
            dragging = false;

        if (dragging) {
            if (diff == 0) {
                ((NumberSetting) getSetting()).setValue(((NumberSetting) getSetting()).getMin());
            } else {
                float newValue = (float) MathUtils.roundDouble(((diff / 84) * (max - min) + min), 2);
                ((NumberSetting) getSetting()).setValue(newValue);
            }
        }

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        renderText(getSetting().getName() + TextFormatting.GRAY + " " + ((NumberSetting) getSetting()).getValue(), (getModuleButton().getPanel().getX() + 4) * 2f, (getModuleButton().getOffset() + getOffset() + 3.5f) * 2f, -1);
        GL11.glPopMatrix();

        RenderUtil.drawRect(getModuleButton().getPanel().getX() + 4, getModuleButton().getOffset() + getOffset() + 10, 84, 1, new Color(30, 30, 30).getRGB());
        RenderUtil.drawRect(getModuleButton().getPanel().getX() + 4, getModuleButton().getOffset() + getOffset() + 10, renderWidth, 1, Colours.mainColour.getColour().getRGB());
        RenderUtil.drawRect(getModuleButton().getPanel().getX() + 3.5f + renderWidth, getModuleButton().getOffset() + getOffset() + 9.5f, 2, 2, new Color(75, 75, 75).getRGB());

        super.renderSetting(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOver(mouseX, mouseY)) {
                // Set dragging state
                dragging = true;
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
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        dragging = false;

        getSettingComponents().forEach(settingComponent -> settingComponent.mouseReleased(mouseX, mouseY, mouseButton));

        super.mouseReleased(mouseX, mouseY, mouseButton);
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
