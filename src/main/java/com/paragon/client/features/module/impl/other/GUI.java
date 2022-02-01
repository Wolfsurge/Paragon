package com.paragon.client.features.module.impl.other;

import com.paragon.client.Paragon;
import com.paragon.client.features.gui.WindowGUI;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ColourSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class GUI extends Module {

    public static BooleanSetting settingOutline = new BooleanSetting("Setting Outline", "Draws an outline around the settings and expanded module", false);
    public static BooleanSetting windowOutline = new BooleanSetting("Window Outline", "Draws an outline around the window", true);
    public static BooleanSetting separator = new BooleanSetting("Separator", "Draws a bar in between the category buttons and the modules", true);

    public static ColourSetting backgroundColour = new ColourSetting("Background Colour", "The colour of the background", new Color(17, 17, 17));
    public static ColourSetting buttonColour = new ColourSetting("Button Colour", "The colour of the buttons", new Color(23, 23, 23));
    public static ColourSetting buttonBackgroundColour = new ColourSetting("Button Background Colour", "The colour of the background of the buttons", new Color(20, 20, 20));

    public GUI() {
        super("GUI", "The GUI of the client", Category.OTHER, Keyboard.KEY_RSHIFT);
        this.addSettings(settingOutline, windowOutline, separator, backgroundColour, buttonColour, buttonBackgroundColour);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new WindowGUI());
        toggle();
    }
}
