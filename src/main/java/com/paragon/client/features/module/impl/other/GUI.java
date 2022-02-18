package com.paragon.client.features.module.impl.other;

import com.paragon.client.Paragon;
import com.paragon.client.features.gui.panel.PanelGUI;
import com.paragon.client.features.gui.window.WindowGUI;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ColourSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class GUI extends Module {

    // Window settings
    public static BooleanSetting window = new BooleanSetting("Window", "Settings for the Window GUI style", false);
    public static BooleanSetting settingOutline = (BooleanSetting) new BooleanSetting("Setting Outline", "Draws an outline around the settings and expanded module", false).setParentSetting(window);
    public static BooleanSetting windowOutline = (BooleanSetting) new BooleanSetting("Window Outline", "Draws an outline around the window", true).setParentSetting(window);
    public static BooleanSetting separator = (BooleanSetting) new BooleanSetting("Separator", "Draws a bar in between the category buttons and the modules", true).setParentSetting(window);

    // Panel settings
    public static BooleanSetting panel = new BooleanSetting("Panel", "Settings for the Panel GUI style", false);
    public static NumberSetting scrollSpeed = (NumberSetting) new NumberSetting("Scroll Speed", "How fast to scroll", 10, 5, 30, 1).setParentSetting(panel);

    // Shared settings
    public static BooleanSetting darkenBackground = new BooleanSetting("Darken Background", "Darkens the background whilst in the GUI", true);
    public static BooleanSetting gradient = new BooleanSetting("Gradient", "Draw a gradient in the background", false);
    public static BooleanSetting pause = new BooleanSetting("Pause Game", "Pause the game whilst in the GUI", false);

    public static ModeSetting style = new ModeSetting("Style", "The style of the GUI", "Window", new String[]{"Window", "Panel"});

    public static ColourSetting backgroundColour = new ColourSetting("Background Colour", "The colour of the background", new Color(17, 17, 17));
    public static ColourSetting buttonColour = new ColourSetting("Button Colour", "The colour of the buttons", new Color(23, 23, 23));
    public static ColourSetting buttonBackgroundColour = new ColourSetting("Button Background Colour", "The colour of the background of the buttons", new Color(20, 20, 20));

    public GUI() {
        super("GUI", "The GUI of the client", Category.OTHER, Keyboard.KEY_RSHIFT);
        this.addSettings(window, panel, darkenBackground, gradient, pause, style, backgroundColour, buttonColour, buttonBackgroundColour);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(getGUI());
        toggle();
    }

    public static GuiScreen getGUI() {
        switch (style.getCurrentMode()) {
            case "Window":
                return Paragon.windowGUI;
            case "Panel":
                return Paragon.panelGUI;
        }

        return Paragon.windowGUI;
    }
}
