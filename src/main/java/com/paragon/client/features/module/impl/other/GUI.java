package com.paragon.client.features.module.impl.other;

import com.paragon.client.features.gui.window.WindowGUI;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ColourSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class GUI extends Module {

    public static BooleanSetting window = new BooleanSetting("Window", "Settings for the Window GUI style", true);
    public static BooleanSetting settingOutline = (BooleanSetting) new BooleanSetting("Setting Outline", "Draws an outline around the settings and expanded module", false).setParentSetting(window);
    public static BooleanSetting windowOutline = (BooleanSetting) new BooleanSetting("Window Outline", "Draws an outline around the window", true).setParentSetting(window);
    public static BooleanSetting separator = (BooleanSetting) new BooleanSetting("Separator", "Draws a bar in between the category buttons and the modules", true).setParentSetting(window);

    // Shared settings
    public static BooleanSetting darkenBackground = new BooleanSetting("Darken Background", "Darkens the background whilst in the GUI", true);
    public static BooleanSetting gradient = new BooleanSetting("Gradient", "Draw a gradient in the background", false);
    public static BooleanSetting pause = new BooleanSetting("Pause Game", "Pause the game whilst in the GUI", false);

    public static ColourSetting backgroundColour = new ColourSetting("Background Colour", "The colour of the background", new Color(17, 17, 17));
    public static ColourSetting buttonColour = new ColourSetting("Button Colour", "The colour of the buttons", new Color(23, 23, 23));
    public static ColourSetting buttonBackgroundColour = new ColourSetting("Button Background Colour", "The colour of the background of the buttons", new Color(20, 20, 20));

    public GUI() {
        super("GUI", "The GUI of the client", Category.OTHER, Keyboard.KEY_RSHIFT);
        this.addSettings(window, darkenBackground, gradient, pause, backgroundColour, buttonColour, buttonBackgroundColour);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new WindowGUI());
        toggle();
    }
}
