package com.paragon.client.features.module.settings.impl;

import com.paragon.client.features.module.settings.Setting;

import java.awt.*;

public class ColourSetting extends Setting {

    private Color colour;

    /**
     * Creates a new colour setting
     * @param name The name of the setting
     * @param description The description of the setting
     * @param defaultColour The default colour
     */
    public ColourSetting(String name, String description, Color defaultColour) {
        setName(name);
        setDescription(description);
        setColour(defaultColour);
    }

    /**
     * Gets the current colour
     */
    public Color getColour() {
        return colour;
    }

    /**
     * Sets the current colour
     * @param newColour The new colour
     */
    public void setColour(Color newColour) {
        this.colour = newColour;
    }

}
