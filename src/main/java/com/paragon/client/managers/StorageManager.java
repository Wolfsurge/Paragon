package com.paragon.client.managers;

import com.paragon.client.Paragon;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.Setting;
import com.paragon.client.features.module.settings.impl.*;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.Sys;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * @author Wolfsurge
 * @since 2/2/22
 */
public class StorageManager {

    public File modulesFolder = new File("paragon/modules/");

    /**
     * Saves a module config to a file
     * @param moduleIn The module
     */
    public void saveModuleConfiguration(Module moduleIn) {
        if(!modulesFolder.exists()) modulesFolder.mkdirs();

        File moduleFile = new File(modulesFolder + "/" + moduleIn.getName() + ".conf");

        try {
            moduleFile.createNewFile();

            PrintWriter printWriter = new PrintWriter(moduleFile);

            printWriter.println("enabled: " + moduleIn.isEnabled());
            printWriter.println("visible: " + moduleIn.isVisible());

            for(Setting setting : moduleIn.getSettings()) {
                if(setting instanceof BooleanSetting) printWriter.println(setting.getName() + ": " + ((BooleanSetting) setting).isEnabled());
                else if(setting instanceof NumberSetting) printWriter.println(setting.getName() + ": " + ((NumberSetting) setting).getValue());
                else if(setting instanceof ModeSetting) printWriter.println(setting.getName() + ": " + ((ModeSetting) setting).getCurrentMode());
                else if(setting instanceof ColourSetting) printWriter.println(setting.getName() + ": " + ((ColourSetting) setting).getColour().getRGB());
                else if(setting instanceof KeybindSetting) printWriter.println(setting.getName() + ": " + ((KeybindSetting) setting).getKeyCode());
            }

            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a module's config
     */
    public void loadModuleConfiguration(Module moduleIn) {
        if(!modulesFolder.exists()) modulesFolder.mkdirs();

        File moduleFile = new File(modulesFolder + "/" + moduleIn.getName() + ".conf");

        try {
            Scanner fileScanner = new Scanner(moduleFile);

            // Check that we have a next line
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                if (line.startsWith("enabled: ")) {
                    line.replace("enabled: ", "");
                    boolean enabled = Boolean.parseBoolean(line);

                    if (enabled) {
                        moduleIn.toggle();
                    }

                    return;
                }

                if (line.startsWith("visible: ")) {
                    line.replace("enabled: ", "");
                    boolean visible = Boolean.parseBoolean(line);

                    moduleIn.setVisible(visible);

                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
