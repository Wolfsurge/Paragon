package com.paragon.client.managers;

import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.impl.combat.AutoTotem;
import com.paragon.client.features.module.impl.hud.HUD;
import com.paragon.client.features.module.impl.movement.Flight;
import com.paragon.client.features.module.impl.movement.Step;
import com.paragon.client.features.module.impl.other.GUI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    public ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        MinecraftForge.EVENT_BUS.register(this);
        init();
    }

    public void init() {
        modules.add(new AutoTotem());

        modules.add(new Step());
        modules.add(new Flight());

        modules.add(new HUD());
        modules.add(new GUI());
    }

    /**
     * Gets the modules in a category
     * @param category The category to get modules from
     */
    public List<Module> getModulesInCategory(Category category) {
        List<Module> categoryModules = new ArrayList<>();
        for(Module m : modules) if(m.getCategory() == category)
            categoryModules.add(m);
        return categoryModules;
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if(Keyboard.getEventKeyState()) {
            for(Module m : modules) {
                if(m.getKeyCode().getKeyCode() == Keyboard.getEventKey() && m.getKeyCode().getKeyCode() > 1) {
                    m.toggle();
                }
            }
        }
    }

}
