package com.paragon.client.features.module.settings;

import java.util.ArrayList;
import java.util.List;

public class Setting {

    /* Name and description of the setting */
    private String name, description;

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
}
