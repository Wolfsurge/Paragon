package com.paragon.client.features.module.impl.hud;

import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.ColourUtil;
import com.paragon.client.Paragon;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HUD extends Module implements TextRenderer {

    private final BooleanSetting watermark = new BooleanSetting("Watermark", "Draws the client's name in the top left", true);

    private final BooleanSetting arrayList = new BooleanSetting("Array List", "Render the enabled modules on screen", true);
    private final ModeSetting arrayListColour = (ModeSetting) new ModeSetting("Array List Colour", "What colour to render the modules in", "Rainbow Wave", new String[]{"Rainbow Wave", "Rainbow", "Static"}).setParentSetting(arrayList);

    private final BooleanSetting info = new BooleanSetting("Info", "Render useful information in the bottom left", true);
    private final BooleanSetting fps = (BooleanSetting) new BooleanSetting("FPS", "Render your FPS", true).setParentSetting(info);

    public HUD() {
        super("HUD", "Render the client's HUD on screen", Category.OTHER);
        this.addSettings(watermark, arrayList, info);
    }

    @SubscribeEvent
    public void onRenderGUI(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        drawWatermark();
        drawInfo();
        drawArrayList();
    }

    public void drawWatermark() {
        if(watermark.isEnabled())
            renderText("Paragon " + TextFormatting.GRAY + Paragon.VERSION, 3, 3, Colours.mainColour.getColour().getRGB());
    }

    public void drawInfo() {
        if (info.isEnabled()) {
            ScaledResolution scaledResolution = new ScaledResolution(mc);

            float y = scaledResolution.getScaledHeight() - 11;

            if (fps.isEnabled()) {
                renderText("FPS " + TextFormatting.GRAY + Minecraft.getDebugFPS(), 2, y, Colours.mainColour.getColour().getRGB());
                y -= 11;
            }
        }
    }

    public void drawArrayList() {
        if(arrayList.isEnabled()) {
            ScaledResolution sr = new ScaledResolution(mc);
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
    }

    public int getArrayListColour(int index) {
        if(arrayListColour.is("Rainbow Wave")) return ColourUtil.getRainbow(4, 1, index);
        else if(arrayListColour.is("Rainbow")) return ColourUtil.getRainbow(4, 1, 0);
        else if(arrayListColour.is("Static")) return Colours.mainColour.getColour().getRGB();
        return -1;
    }
}
