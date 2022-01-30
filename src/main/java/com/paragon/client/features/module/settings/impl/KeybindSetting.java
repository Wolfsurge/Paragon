package com.paragon.client.features.module.settings.impl;

import com.paragon.client.features.module.settings.Setting;

public class KeybindSetting extends Setting {

    private int keyCode;

    public KeybindSetting(String name, String description, int defaultKeyCode) {
        setName(name);
        setDescription(description);
        setKeyCode(defaultKeyCode);
    }

    /**
     * Gets the setting's keycode
     * @return The setting's keycode
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Sets the setting's keycode
     * @param keyCodeIn The new keycode
     */
    public void setKeyCode(int keyCodeIn) {
        this.keyCode = keyCodeIn;
    }

}
