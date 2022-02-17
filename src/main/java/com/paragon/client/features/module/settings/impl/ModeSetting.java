package com.paragon.client.features.module.settings.impl;

import com.paragon.client.features.module.settings.Setting;

public class ModeSetting extends Setting {

    private String[] modes;
    private int currentMode;

    public ModeSetting(String name, String description, String defaultMode, String[] modes) {
        setName(name);
        setDescription(description);
        setModes(modes);
        setCurrentMode(defaultMode);
        this.modes = modes;
    }

    /**
     * Sets the mode to the next one
     */
    public void cycleMode() {
        // If the mode is at the end, set the mode to the beginning
        if(currentMode == modes.length - 1) currentMode = 0;
        // Otherwise, just increase it
        else currentMode++;
    }

    /**
     * Returns whether the current mode is equal to a given mode
     * @param mode The given mode to compare to
     * @return Whether the mode is the given mode
     */
    public boolean is(String mode) {
        return getCurrentMode().equalsIgnoreCase(mode);
    }

    /**
     * Sets the available modes
     * @param modes The modes
     */
    public void setModes(String[] modes) {
        this.modes = modes;
    }

    /**
     * Gets the current mode
     * @return The current mode
     */
    public String getCurrentMode() {
        return modes[currentMode];
    }

    /**
     * Sets the current mode
     * @param newCurrentMode The new mode
     */
    public void setCurrentMode(String newCurrentMode) {
        int index = 0;
        for(String str : modes) {
            if(str.equalsIgnoreCase(newCurrentMode)) {
                break;
            }

            index++;
        }

        // Return if we couldn't find the given mode
        if (index + 1 > modes.length) {
            return;
        }

        this.currentMode = index;
    }
}
