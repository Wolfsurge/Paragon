package com.paragon.client.features.module.settings.impl;

import com.paragon.client.features.module.settings.Setting;

/**
 * @author Wolfsurge
 */

public class BooleanSetting extends Setting {

    /* Is the setting enabled */
    private boolean enabled;

    /**
     * Creates a new boolean setting
     * @param name The name of the setting
     * @param description The description of the setting
     * @param defaultState Whether the setting is enabled by default
     */
    public BooleanSetting(String name, String description, boolean defaultState) {
        setName(name);
        setDescription(description);
        setEnabled(defaultState);
    }

    /**
     * Gets whether the setting is enabled or not
     * @return The setting's state
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the setting is enabled
     * @param enabledIn The new state
     */
    public void setEnabled(boolean enabledIn) {
        this.enabled = enabledIn;
    }

}
