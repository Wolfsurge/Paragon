package com.paragon.client;

import com.paragon.client.features.gui.console.Console;
import com.paragon.client.features.gui.panel.PanelGUI;
import com.paragon.client.features.gui.taskbar.Taskbar;
import com.paragon.client.features.gui.window.WindowGUI;
import com.paragon.client.managers.CommandManager;
import com.paragon.client.managers.ModuleManager;
import com.paragon.client.managers.RotationManager;
import com.paragon.client.managers.social.SocialManager;
import com.paragon.client.managers.storage.StorageManager;
import me.zero.alpine.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Paragon.MODID, name = Paragon.NAME, version = Paragon.VERSION)
public class Paragon {
    public static final String MODID = "paragon";
    public static final String NAME = "Paragon";
    public static final String VERSION = "1.0";

    private static Logger logger;

    public static EventManager EVENT_BUS = new EventManager();

    public static ModuleManager moduleManager;
    public static StorageManager storageManager;
    public static RotationManager rotationManager;
    public static Taskbar taskbar;
    public static SocialManager socialManager;
    public static CommandManager commandManager;

    // GUIs
    public static WindowGUI windowGUI;
    public static PanelGUI panelGUI;
    public static Console console;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Create managers
        storageManager = new StorageManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        taskbar = new Taskbar();

        socialManager = new SocialManager();
        storageManager.loadSocial();

        commandManager = new CommandManager();

        windowGUI = new WindowGUI();
        panelGUI = new PanelGUI();
        console = new Console("Paragon Console", 400, 350);

        logger.info("[PARAGON] Initialised.");
    }
}
