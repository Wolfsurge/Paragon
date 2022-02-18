package com.paragon.client.features.gui.console;

import com.paragon.api.util.Wrapper;
import com.paragon.api.util.miscellaneous.TextRenderer;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import com.paragon.client.managers.CommandManager;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Console implements Wrapper, TextRenderer {

    // Title of window
    private String title;

    // Coordinates and dimensions
    private float width, height;

    // List of lines
    private List<String> lines = new ArrayList<>();

    private GuiTextField guiTextField;

    public Console(String title, float width, float height) {
        this.title = title;
        this.width = width;
        this.height = height;

        ScaledResolution scaledResolution = new ScaledResolution(mc);

        guiTextField = new GuiTextField(0, mc.fontRenderer, (int) ((scaledResolution.getScaledWidth() / 2) - (getWidth() / 2)) + 2, (int) (((scaledResolution.getScaledHeight() / 2) - (getHeight() / 2)) + getHeight() - 11), (int) getWidth(), 11);
    }

    public void init() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        guiTextField = new GuiTextField(0, mc.fontRenderer, (int) ((scaledResolution.getScaledWidth() / 2) - (getWidth() / 2)) + 2, (int) (((scaledResolution.getScaledHeight() / 2) - (getHeight() / 2)) + getHeight() - 13), (int) getWidth() - 4, 11);
    }

    public void draw(int mouseX, int mouseY) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        // Background
        RenderUtil.drawRect((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f), (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f), getWidth(), getHeight(), 0x90000000);

        RenderUtil.drawRect((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f), (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f), getWidth(), 14, GUI.buttonColour.getColour().getRGB());

        // Title
        renderText(getTitle(), ((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f)) + 3, ((scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f)) + 3, -1);

        // Border
        RenderUtil.drawBorder((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f), (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f), getWidth(), getHeight(), 1, Colours.mainColour.getColour().getRGB());

        // Scissor
        RenderUtil.startGlScissor((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f), (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f) + 13, getWidth(), getHeight() - 26.5f);

        float lineY = (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f) + getHeight() - 26;

        Collections.reverse(lines);

        for (String string : lines) {
            renderText(string, (scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f) + 2, lineY, -1);
            lineY -= 11;
        }

        Collections.reverse(lines);

        // End scissor
        RenderUtil.endGlScissor();

        guiTextField.drawTextBox();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        guiTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_RETURN) {
            CommandManager.handleCommands(guiTextField.getText(), true);

            guiTextField.setText("");
            guiTextField.setFocused(false);
            return;
        }

        guiTextField.textboxKeyTyped(typedChar, keyCode);
    }

    public void addLine(String line) {
        lines.add(line);
    }

    /**
     * Gets the title
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the width
     * @return The width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height
     * @return The height
     */
    public float getHeight() {
        return height;
    }
}