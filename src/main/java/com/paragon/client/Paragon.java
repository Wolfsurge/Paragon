package com.paragon.client;

import com.paragon.client.features.gui.panel.PanelGUI;
import com.paragon.client.features.gui.window.WindowGUI;
import com.paragon.client.managers.ModuleManager;
import com.paragon.client.managers.RotationManager;
import com.paragon.client.managers.storage.StorageManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Paragon.MODID, name = Paragon.NAME, version = Paragon.VERSION)
public class Paragon
{
    public static final String MODID = "paragon";
    public static final String NAME = "Paragon";
    public static final String VERSION = "DEV 300122";

    private static Logger logger;

    public static ModuleManager moduleManager;
    public static StorageManager storageManager;

    public static RotationManager rotationManager;

    // GUIs
    public static WindowGUI windowGUI;
    public static PanelGUI panelGUI;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Create managers
        storageManager = new StorageManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();

        windowGUI = new WindowGUI();
        panelGUI = new PanelGUI();

        logger.info("[PARAGON] Initialised.");
    }
}
