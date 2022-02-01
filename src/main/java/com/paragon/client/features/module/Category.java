package com.paragon.client.features.module;

public enum Category {
    /**
     * Modules associated with PVP, e.g. AutoCrystal, Offhand
     */
    COMBAT("Combat"),

    /**
     * Modules associated with Movement, e.g. Step, ElytraFly
     */
    MOVEMENT("Movement"),

    /**
     * Modules that render things, e.g. ESP, Tracers
     */
    RENDER("Render"),

    /**
     * Modules that don't belong to a particular category, e.g. AutoEZ, ChatSuffix
     */
    MISC("Misc"),

    /**
     * Modules that aren't really modules in the usual sense
     */
    OTHER("Other");

    /* /**
     * Modules that are drawn on the screen

    HUD("HUD"); */

    /**
     * Creates a new category
     * @param name The name of the category
     */
    Category(String name) {
        setName(name);
    }

    /* The name of the category*/
    private String name;

    /**
     * Gets the category's name
     * @return The category's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the category's name
     * @param name The category's name
     */
    public void setName(String name) {
        this.name = name;
    }
}
