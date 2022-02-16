package com.paragon.client.features.module;

import com.paragon.client.Paragon;
import com.paragon.client.features.module.settings.Setting;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.KeybindSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Wolfsurge
 */

public class Module {

    // Name of module
    private final String name;

    // Description of module
    private final String description;

    // Category of module
    private Category category;

    // Module Settings
    private final List<Setting> settings = new ArrayList<>();
    private final KeybindSetting keyCode = new KeybindSetting("Keybind", "The keybind of the module", 0);

    // Module state
    private boolean enabled;
    private boolean visible = true;

    protected Minecraft mc = Minecraft.getMinecraft();

    /**
     * Creates a new module
     * @param name The name of the module
     * @param description The description of the module
     * @param category The category of the module
     */
    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;

        addSettings(keyCode);

        // Load configuration
        Paragon.storageManager.loadModuleConfiguration(this);
    }

    /**
     * Creates a new module with a set keybind
     * @param name The name of the module
     * @param description The description of the module
     * @param category The category of the module
     * @param keyBind The default keybind
     */
    public Module(String name, String description, Category category, int keyBind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keyCode.setKeyCode(keyBind);

        addSettings(keyCode);

        // Load configuration
        Paragon.storageManager.loadModuleConfiguration(this);
    }

    /**
     * Add settings to the module
     * @param settings An undefined amount of settings to add
     */
    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings)); // Add settings
        this.settings.sort(Comparator.comparingInt(s -> s == keyCode ? 1 : 0)); // Make keybind be last
    }

    /**
     * Enable module
     */
    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
        Paragon.EVENT_BUS.subscribe(this);
        onEnable();
    }

    /**
     * Disable module
     */
    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        Paragon.EVENT_BUS.unsubscribe(this);
        onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}

    /**
     * Toggle module state
     */
    public void toggle() {
        enabled = !enabled;

        if(enabled)
            enable();
        else
            disable();
    }

    /**
     * Gets the module's name
     * @return The module's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the module's description
     * @return The module's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the module's category
     * @return The module's category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the module's category
     * @param category The category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Gets the module's settings
     * @return The module's settings
     */
    public List<Setting> getSettings() {
        return settings;
    }

    /**
     * Gets the module's keybind
     * @return The module's keybind
     */
    public KeybindSetting getKeyCode() {
        return keyCode;
    }

    /**
     * Gets whether the module is enabled
     * @return The module's state
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the module's state
     * @param enabled New module state
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if(enabled)
            enable();
        else
            disable();
    }

    /**
     * Gets whether the module is visible
     * @return Whether the module is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets whether the module is visible
     * @param visible New visibility
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets the module's info for the Array List
     * @return The module's info
     */
    public String getModuleInfo() {
        return "";
    }

    /**
     * Checks if the player or the world is null
     * @return If the player or the world is null
     */
    public boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }
}
