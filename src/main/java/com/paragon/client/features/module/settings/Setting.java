package com.paragon.client.features.module.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Setting {

    /* Name and description of the setting */
    private String name, description;

    /* All the setting's subsettings */
    private final List<Setting> subsettings = new ArrayList<>();

    /* The parent setting. If null, it doesn't have a parent. Not yet implemented */
    private Setting parentSetting;

    /* Whether we can see and interact with the setting in the GUIs */
    private Supplier<Boolean> visible = () -> true;

    /**
     * Gets the setting's name
     * @return The setting's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the setting's name
     * @param nameIn The name to set it to
     */
    public void setName(String nameIn) {
        this.name = nameIn;
    }

    /**
     * Gets the setting's description
     * @return The setting's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the setting's description
     * @param descriptionIn The description to set it to
     */
    public void setDescription(String descriptionIn) {
        this.description = descriptionIn;
    }

    /**
     * Gets the subsettings
     * @return The subsettings
     */
    public List<Setting> getSubsettings() {
        return subsettings;
    }

    /**
     * Gets the parent setting
     * @return The parent setting
     */
    public Setting getParentSetting() {
        return parentSetting;
    }

    /**
     * Sets the parent setting
     * @param parentSetting The new parent setting
     */
    public Setting setParentSetting(Setting parentSetting) {
        this.parentSetting = parentSetting;

        parentSetting.getSubsettings().add(this);

        return this;
    }

    /**
     * Gets whether the setting is able to be interacted with
     * @return Whether the setting is able to be interacted with
     */
    public boolean isVisible() {
        return visible.get();
    }

    /**
     * Sets the visibility
     * @param visible Is the setting visible
     * @return The setting
     */
    public Setting setVisiblity(Supplier<Boolean> visible) {
        this.visible = visible;

        return this;
    }
}
