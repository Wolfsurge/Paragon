package com.paragon.client.features.gui.taskbar;

import com.paragon.api.util.Wrapper;
import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import net.minecraft.client.gui.ScaledResolution;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Wolfsurge
 */
public class Taskbar implements Wrapper, TextRenderer {

    public void drawTaskbar(int mouseX, int mouseY) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        RenderUtil.drawRect(0, scaledResolution.getScaledHeight() - 50, scaledResolution.getScaledWidth(), 50, GUI.backgroundColour.getColour().getRGB());
        RenderUtil.drawRect(0, scaledResolution.getScaledHeight() - 52, scaledResolution.getScaledWidth(), 2, Colours.mainColour.getColour().getRGB());

        glPushMatrix();
        glScalef(2f, 2f, 2f);
        renderText("Paragon", 2, (scaledResolution.getScaledHeight() - 33) / 2f, Colours.mainColour.getColour().getRGB());
        glPopMatrix();
    }

}
