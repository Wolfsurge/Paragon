package com.paragon.client.managers.storage;

import com.paragon.client.Paragon;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.Setting;
import com.paragon.client.features.module.settings.impl.*;
import com.paragon.client.managers.social.Player;
import com.paragon.client.managers.social.Relationship;
import jdk.nashorn.internal.parser.JSONParser;
import net.minecraftforge.event.world.NoteBlockEvent;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import scala.xml.PrettyPrinter;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wolfsurge
 * @since 2/2/22
 */
public class StorageManager {

    public File modulesFolder = new File("paragon/modules/");
    public File friendsFolder = new File("paragon/friends");

    /**
     * Saves a module config to a file
     * @param moduleIn The module
     */
    public void saveModuleConfiguration(Module moduleIn) {
        if(!modulesFolder.exists()) modulesFolder.mkdirs();

        try {
            File file = new File("paragon/modules/" + moduleIn.getName() + ".json");
            JSONObject jsonObject = new JSONObject();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);

            try {
                jsonObject.put("enabled", moduleIn.isEnabled());
                jsonObject.put("visible", moduleIn.isVisible());

                for (Setting setting : moduleIn.getSettings()) {
                    if (setting instanceof BooleanSetting) jsonObject.put(setting.getName(), ((BooleanSetting) setting).isEnabled());
                    else if (setting instanceof NumberSetting) jsonObject.put(setting.getName(), ((NumberSetting) setting).getValue());
                    else if (setting instanceof ModeSetting) jsonObject.put(setting.getName(), ((ModeSetting) setting).getCurrentMode());
                    else if (setting instanceof ColourSetting) jsonObject.put(setting.getName(), ((ColourSetting) setting).getColour().getRGB());
                    else if (setting instanceof KeybindSetting) jsonObject.put(setting.getName(), ((KeybindSetting) setting).getKeyCode());

                    if (!setting.getSubsettings().isEmpty()) {
                        setting.getSubsettings().forEach(setting1 -> {
                            String settingName = setting1.getParentSetting().getName() + "-" + setting1.getName();

                            try {
                                if (setting1 instanceof BooleanSetting)
                                    jsonObject.put(settingName, ((BooleanSetting) setting1).isEnabled());
                                else if (setting1 instanceof NumberSetting)
                                    jsonObject.put(settingName, ((NumberSetting) setting1).getValue());
                                else if (setting1 instanceof ModeSetting)
                                    jsonObject.put(settingName, ((ModeSetting) setting1).getCurrentMode());
                                else if (setting1 instanceof ColourSetting)
                                    jsonObject.put(settingName, ((ColourSetting) setting1).getColour().getRGB());
                                else if (setting1 instanceof KeybindSetting)
                                    jsonObject.put(settingName, ((KeybindSetting) setting1).getKeyCode());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }

                fileWriter.write(jsonObject.toString(4));
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a module's config
     */
    public void loadModuleConfiguration(Module moduleIn) {
        if(!modulesFolder.exists()) modulesFolder.mkdirs();

        try {
            JSONObject jsonObject = loadExistingConfiguration(new File("paragon/modules/" + moduleIn.getName() + ".json"));

            // Toggle module if it's enabled
            if (jsonObject.getBoolean("enabled")) {
                moduleIn.setEnabled(true);
            }

            moduleIn.setVisible(jsonObject.getBoolean("visible"));

            for (Setting setting : moduleIn.getSettings()) {
                if (setting instanceof BooleanSetting) ((BooleanSetting) setting).setEnabled(jsonObject.getBoolean(setting.getName()));
                else if (setting instanceof NumberSetting) ((NumberSetting) setting).setValue(jsonObject.getLong(setting.getName()));
                else if (setting instanceof ModeSetting) ((ModeSetting) setting).setCurrentMode(jsonObject.getString(setting.getName()));
                else if (setting instanceof ColourSetting) ((ColourSetting) setting).setColour(new Color(jsonObject.getInt(setting.getName())));
                else if (setting instanceof KeybindSetting) ((KeybindSetting) setting).setKeyCode(jsonObject.getInt(setting.getName()));

                for (Setting subSetting : setting.getSubsettings()) {
                    String settingName = subSetting.getParentSetting().getName() + "-" + subSetting.getName();

                    if (subSetting instanceof BooleanSetting) ((BooleanSetting) subSetting).setEnabled(jsonObject.getBoolean(settingName));
                    else if (subSetting instanceof NumberSetting) ((NumberSetting) subSetting).setValue(jsonObject.getLong(settingName));
                    else if (subSetting instanceof ModeSetting) ((ModeSetting) subSetting).setCurrentMode(jsonObject.getString(settingName));
                    else if (subSetting instanceof ColourSetting) ((ColourSetting) subSetting).setColour(new Color(jsonObject.getInt(settingName)));
                    else if (subSetting instanceof KeybindSetting) ((KeybindSetting) subSetting).setKeyCode(jsonObject.getInt(settingName));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSocial() {
        if(!friendsFolder.exists()) friendsFolder.mkdirs();

        try {
            File file = new File("paragon/friends/friends.json");
            JSONObject jsonObject = new JSONObject();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);

            try {
                JSONArray array = new JSONArray();

                for (Player player : Paragon.socialManager.players) {
                    array.put(player.getName() + ":" + player.getRelationship().name());
                }

                jsonObject.putOpt("acquaintances", array);

                fileWriter.write(jsonObject.toString(4));
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSocial() {
        if(!friendsFolder.exists()) friendsFolder.mkdirs();

        try {
            JSONObject jsonObject = loadExistingConfiguration(new File("paragon/friends/friends.json"));

            JSONArray acquaintances = jsonObject.getJSONArray("acquaintances");

            for (int i = 0; i < acquaintances.length(); i++) {
                String[] info = String.valueOf(acquaintances.get(i)).split(":");
                Player player = new Player(info[0], Relationship.valueOf(info[1]));
                Paragon.socialManager.addPlayer(player);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject loadExistingConfiguration(File file) throws IOException, JSONException {
        return new JSONObject(FileUtils.readFileToString(file, Charsets.UTF_8));
    }

}
