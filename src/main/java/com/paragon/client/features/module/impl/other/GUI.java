package com.paragon.client.features.module.impl.other;

import com.paragon.client.Paragon;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import org.lwjgl.input.Keyboard;

public class GUI extends Module {

    public static BooleanSetting settingOutline = new BooleanSetting("Setting Outline", "Draws an outline around the settings and expanded module", false);

    public GUI() {
        super("ClickGUI", "The GUI of the client", Category.OTHER, Keyboard.KEY_RSHIFT);
        this.addSettings(settingOutline);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Paragon.windowGUI);
        toggle();
    }
}
