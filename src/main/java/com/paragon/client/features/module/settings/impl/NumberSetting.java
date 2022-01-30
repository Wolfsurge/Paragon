package com.paragon.client.features.module.settings.impl;

import com.paragon.client.features.module.settings.Setting;

public class NumberSetting extends Setting {

    /* The minimum, maximum, default value, and increment of the setting */
    protected float min, max, value, increment;

    /**
     * Creates a new number setting
     * @param name Name of the setting
     * @param description Description of the setting
     * @param value Default Value
     * @param min Minimum Value
     * @param max Maximum Value
     * @param increment The amount to increment by
     */
    public NumberSetting(String name, String description, float value, float min, float max, float increment) {
        setName(name);
        setDescription(description);
        setMin(min);
        setMax(max);
        setIncrement(increment);
        setValue(value);
    }

    /**
     * Gets the minimum value
     * @return The minimum value
     */
    public float getMin() {
        return min;
    }

    /**
     * Sets the minimum value
     * @param min The new minimum value
     */
    public void setMin(float min) {
        this.min = min;
    }

    /**
     * Gets the maximum value
     * @return The maximum value
     */
    public float getMax() {
        return max;
    }

    /**
     * Sets the maximum value
     * @param max The new maximum value
     */
    public void setMax(float max) {
        this.max = max;
    }

    /**
     * Gets the current value
     * @return The current value
     */
    public float getValue() {
        return this.value;
    }

    /**
     * Sets the current value.
     * @param value The new value
     */
    public void setValue(float value) {
        float precision = 1 / increment;
        this.value = Math.round(Math.max(getMin(), Math.min(getMax(), value)) * precision) / precision;
    }

    /**
     * Gets the increment
     * @return The increment
     */
    public float getIncrement() {
        return increment;
    }

    /**
     * Sets the increment
     * @param increment The new increment
     */
    public void setIncrement(float increment) {
        this.increment = increment;
    }
}