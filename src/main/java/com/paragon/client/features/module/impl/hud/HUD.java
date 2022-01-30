package com.paragon.client.features.module.impl.hud;

import com.paragon.api.event.events.RenderGUIEvent;
import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.ColourUtil;
import com.paragon.client.Paragon;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HUD extends Module implements TextRenderer {

    private final BooleanSetting arrayList = new BooleanSetting("Array List", "Render the enabled modules on screen", true);
    private final ModeSetting arrayListColour = new ModeSetting("Array List Colour", "What colour to render the modules in", "Rainbow Wave", new String[]{"Rainbow Wave", "Rainbow", "Static"});

    public HUD() {
        super("HUD", "Render the client's HUD on screen", Category.HUD);
        this.addSettings(arrayList, arrayListColour);
        this.toggle();
    }

    @EventHandler
    private final Listener<RenderGUIEvent> renderGUIEventListener = new Listener<>(event -> {
        ScaledResolution sr = new ScaledResolution(mc);

        if(arrayList.isEnabled()) {
            float y = sr.getScaledHeight() - 11;

            int index = 0;

            // creating a new list, so we can sort these, but not the module manager list
            List<Module> modules = new ArrayList<>();
            for(Module m : Paragon.moduleManager.modules) if (m.isEnabled() && m.isVisible()) modules.add(m);

            // Sort by module length
            modules.sort(Comparator.comparingDouble(module -> getStringWidth(((Module) module).getName() + ((Module) module).getModuleInfo())).reversed());

            for(Module m : modules) {
                renderText(m.getName() + TextFormatting.GRAY + m.getModuleInfo(), sr.getScaledWidth() - getStringWidth(m.getName() + m.getModuleInfo()) - 1, y, getArrayListColour(index * 150));
                y -= 11;
                index++;
            }
        }

    });

    public int getArrayListColour(int index) {
        if(arrayListColour.is("Rainbow Wave")) return ColourUtil.getRainbow(4, 1, index);
        else if(arrayListColour.is("Rainbow")) return ColourUtil.getRainbow(4, 1, 0);
        else if(arrayListColour.is("Static")) return Paragon.mainColour;
        return -1;
    }
}
