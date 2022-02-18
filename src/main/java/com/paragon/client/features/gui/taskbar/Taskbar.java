package com.paragon.client.features.gui.taskbar;

import com.paragon.api.util.Wrapper;
import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.Paragon;
import com.paragon.client.features.gui.console.ConsoleGUI;
import com.paragon.client.features.gui.taskbar.icons.Icon;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Wolfsurge
 */
public class Taskbar implements Wrapper, TextRenderer {

    private ArrayList<Icon> icons = new ArrayList<>();

    public Taskbar() {
        int x = (int) (2 + (getStringWidth("Paragon") * 2) + 16);
        icons.add(new Icon("GUI", "gui", x, GUI::getGUI));
        x += 44;
        icons.add(new Icon("Console", "console", x, ConsoleGUI::new));
    }

    public void drawTaskbar(int mouseX, int mouseY) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        RenderUtil.drawRect(0, scaledResolution.getScaledHeight() - 50, scaledResolution.getScaledWidth(), 50, GUI.backgroundColour.getColour().getRGB());
        RenderUtil.drawRect(0, scaledResolution.getScaledHeight() - 52, scaledResolution.getScaledWidth(), 2, Colours.mainColour.getColour().getRGB());

        glPushMatrix();
        glScalef(2f, 2f, 2f);
        renderText("Paragon", 2, (scaledResolution.getScaledHeight() - 33) / 2f, Colours.mainColour.getColour().getRGB());
        glPopMatrix();

        for (Icon icon : icons) {
            icon.draw(mouseX, mouseY);
        }
    }

    public void mouseClicked(int mouseX, int mouseY) {
        icons.forEach(icon -> {
            icon.whenClicked(mouseX, mouseY);
        });
    }

}
