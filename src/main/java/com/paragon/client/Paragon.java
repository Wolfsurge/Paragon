package com.paragon.client;

import com.paragon.api.event.EventParser;
import com.paragon.client.features.gui.WindowGUI;
import com.paragon.client.managers.ModuleManager;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.awt.*;

@Mod(modid = Paragon.MODID, name = Paragon.NAME, version = Paragon.VERSION)
public class Paragon
{
    public static final String MODID = "paragon";
    public static final String NAME = "Paragon";
    public static final String VERSION = "DEV 300122";

    private static Logger logger;

    public static EventManager EVENT_BUS = new EventManager();
    public static ModuleManager moduleManager;
    public static WindowGUI windowGUI;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        EventParser.INSTANCE.initialise();
        moduleManager = new ModuleManager();
        windowGUI = new WindowGUI();

        logger.info("[PARAGON] Initialised.");
    }
}
