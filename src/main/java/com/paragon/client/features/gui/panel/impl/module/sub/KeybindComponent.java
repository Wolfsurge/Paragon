package com.paragon.client.features.gui.panel.impl.module.sub;

import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.panel.impl.module.ModuleButton;
import com.paragon.client.features.module.impl.other.GUI;
import com.paragon.client.features.module.settings.impl.KeybindSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class KeybindComponent extends SettingComponent {

    private boolean isListening = false;

    public KeybindComponent(ModuleButton moduleButton, KeybindSetting setting, float offset, float height) {
        super(moduleButton, setting, offset, height);
    }

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        RenderUtil.drawRect(getModuleButton().getPanel().getX(), getModuleButton().getOffset() + getOffset(), getModuleButton().getPanel().getWidth(), getHeight(), isMouseOver(mouseX, mouseY) ? GUI.buttonColour.getColour().brighter().getRGB() : GUI.buttonColour.getColour().getRGB());

        String key = Keyboard.getKeyName(((KeybindSetting) getSetting()).getKeyCode());
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        renderText(getSetting().getName() + TextFormatting.GRAY + (isListening ?  "..." : " " + key), (getModuleButton().getPanel().getX() + 4) * 2, (getModuleButton().getOffset() + getOffset() + 4.5f) * 2, -1);
        GL11.glPopMatrix();

        super.renderSetting(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOver(mouseX, mouseY)) {
                // Set listening
                isListening = !isListening;
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
    public void keyTyped(char typedChar, int keyCode) {
        if (isListening) {
            isListening = false;

            if (keyCode == (Keyboard.KEY_DELETE | Keyboard.KEY_BACK)) {
                ((KeybindSetting) getSetting()).setKeyCode(0);
                return;
            }

            ((KeybindSetting) getSetting()).setKeyCode(keyCode);
        }

        if (isExpanded()) {
            getSettingComponents().forEach(settingComponent -> settingComponent.keyTyped(typedChar, keyCode));
        }
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
