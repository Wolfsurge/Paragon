package com.paragon.client.features.gui.panel.impl.module.sub;

import com.paragon.api.util.render.ColourUtil;
import com.paragon.api.util.render.GuiUtil;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.gui.panel.impl.module.ModuleButton;
import com.paragon.client.features.module.impl.other.Colours;
import com.paragon.client.features.module.impl.other.GUI;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ColourSetting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ColourComponent extends SettingComponent {

    private boolean open = false;
    private boolean pickingColor = false, pickingHue = false, pickingAlpha = false;

    public ColourComponent(ModuleButton moduleButton, ColourSetting setting, float offset, float height) {
        super(moduleButton, setting, offset, height);
    }

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        RenderUtil.drawRect(getModuleButton().getPanel().getX(), getModuleButton().getOffset() + getOffset(), getModuleButton().getPanel().getWidth(), getHeight(), isMouseOver(mouseX, mouseY) ? GUI.buttonColour.getColour().brighter().getRGB() : GUI.buttonColour.getColour().getRGB());

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        renderText(getSetting().getName(), (getModuleButton().getPanel().getX() + 4) * 2f, (getModuleButton().getOffset() + getOffset() + 4.5f) * 2f, -1);
        GL11.glPopMatrix();

        // Draw picker if expanded
        if (open) {
            drawPicker(getModuleButton().getPanel().getX() + 4, getModuleButton().getOffset() + getOffset() + 12,
                    getModuleButton().getPanel().getX() + 4, getModuleButton().getOffset() + getOffset() + 12 + 78,
                    getModuleButton().getPanel().getX() + 4, getModuleButton().getOffset() + getOffset() + 12 + 78 + 13,
                    mouseX, mouseY);
        }

        super.renderSetting(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (GuiUtil.mouseOver(getModuleButton().getPanel().getX(), getModuleButton().getOffset() + getOffset(), getModuleButton().getPanel().getX() + getModuleButton().getPanel().getWidth(), getModuleButton().getOffset() + getOffset() + 12, mouseX, mouseY)) {
            // Toggle open state
            open = !open;
        }
    }

    @Override
    public float getHeight() {
        return open ? 118 : 12;
    }

    @Override
    public float getAbsoluteHeight() {
        float subsettingHeight = 0;

        for (SettingComponent settingComponent : getSettingComponents()) {
            subsettingHeight += settingComponent.getHeight();
        }

        return isExpanded() ? getHeight() + subsettingHeight : getHeight();
    }

    // The rest of the code is from Wurst +3, but edited

    public void drawPicker(float pickerX, float pickerY, float hueSliderX, float hueSliderY, float alphaSliderX, float alphaSliderY, int mouseX, int mouseY) {
        float[] color = new float[] {
                0, 0, 0, 0
        };

        ColourSetting colourSetting = (ColourSetting) getSetting();

        try {
            color = new float[] {Color.RGBtoHSB(colourSetting.getColour().getRed(), colourSetting.getColour().getGreen(), colourSetting.getColour().getBlue(), null)[0], Color.RGBtoHSB(colourSetting.getColour().getRed(), colourSetting.getColour().getGreen(), colourSetting.getColour().getBlue(), null)[1], Color.RGBtoHSB(colourSetting.getColour().getRed(), colourSetting.getColour().getGreen(), colourSetting.getColour().getBlue(), null)[2], colourSetting.getColour().getAlpha() / 255f};
        } catch (Exception ignored) {}

        float pickerWidth = 83;
        float pickerHeight = 75;
        float hueSliderWidth = 83;
        float hueSliderHeight = 12;
        float alphaSliderWidth = 83;
        float alphaSliderHeight = 12;

        if (!pickingColor && !pickingHue && !pickingAlpha) {
            if (Mouse.isButtonDown(0) && GuiUtil.mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY)) {
                pickingColor = true;
            } else if (Mouse.isButtonDown(0) && GuiUtil.mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight, mouseX, mouseY)) {
                pickingHue = true;
            } else if (Mouse.isButtonDown(0) && GuiUtil.mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + alphaSliderWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY))
                pickingAlpha = true;
        }

        if (!Mouse.isButtonDown(0)) {
            pickingColor = false;
            pickingHue = false;
            pickingAlpha = false;
        }

        if (pickingHue) {
            float restrictedX = Math.min(Math.max(hueSliderX, mouseX), hueSliderX + hueSliderWidth);
            color[0] = (restrictedX - hueSliderX) / hueSliderWidth;
            color[0] = (float) Math.min(0.999, color[0]);
        }

        if (pickingAlpha) {
            float restrictedX = Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + alphaSliderWidth);
            color[3] = 1 - (restrictedX - alphaSliderX) / alphaSliderWidth;
        }

        if (pickingColor) {
            float restrictedX = Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
            color[1] = (restrictedX - pickerX) / pickerWidth;
            color[2] = 1 - (restrictedY - pickerY) / pickerHeight;
            color[2] = (float) Math.max(0.04000002, color[2]);
            color[1] = (float) Math.max(0.022222223, color[1]);
        }

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, 255);

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        RenderUtil.drawRect(cursorX - 2, cursorY - 2, 4, 4, Colours.mainColour.getColour().getRGB());

        Color finalColor = ColourUtil.integrateAlpha(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);

        drawAlphaSlider(alphaSliderX, alphaSliderY, alphaSliderWidth, alphaSliderHeight, finalColor.getRed() / 255f, finalColor.getGreen() / 255f, finalColor.getBlue() / 255f, color[3]);

        colourSetting.setColour(finalColor);
    }

    public void drawHueSlider(float x, float y, float width, float height, float hue) {
        int step = 0;
        if (height > width) {
            RenderUtil.drawSelectRect(x, y, x + width, y + 4, 0xFFFF0000);
            y += 4;

            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                // RenderUtil.drawGradientRect(x, y + step * (height / 6f), x + width, y + (step + 1) * (height / 6f), previousStep, nextStep, false);
                step++;
            }
            int sliderMinY = (int) (y + height*hue) - 4;
            RenderUtil.drawSelectRect(x, sliderMinY - 1, x + width, sliderMinY + 1,-1);
        } else {
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                RenderUtil.leftGradient(x + step * (width / 6), y, x + (step + 1) * (width / 6), y + height, previousStep, nextStep);
                step++;
            }

            int sliderMinX = (int) (x + (width * hue));
            RenderUtil.drawSelectRect(sliderMinX - 0.5f, y, sliderMinX + 0.5f, y + height, -1);
        }
    }

    public void drawAlphaSlider(float x, float y, float width, float height, float red, float green, float blue, float alpha) {
        boolean left = true;
        float checkerBoardSquareSize = height / 2;

        for (float squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                RenderUtil.drawSelectRect(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0xFFFFFFFF);
                RenderUtil.drawSelectRect(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0xFF909090);

                if (squareIndex < width - checkerBoardSquareSize) {
                    float minX = x + squareIndex + checkerBoardSquareSize;
                    float maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    RenderUtil.drawSelectRect(minX, y, maxX, y + height, 0xFF909090);
                    RenderUtil.drawSelectRect(minX, y + checkerBoardSquareSize, maxX, y + height,0xFFFFFFFF);
                }
            }

            left = !left;
        }

        RenderUtil.leftGradient(x, y, x + width, y + height, new Color(red, green, blue, 1).getRGB(), 0);
        int sliderMinX = (int) (x + width - (width * alpha));
        RenderUtil.drawRect(sliderMinX - 0.5f, y, 1, height, -1);
    }

    public static void drawPickerBase(double px, double py, double pw, double ph, float red, float green, float blue, float alpha) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_POLYGON);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float pickerX = (float) px;
        float pickerY = (float) py;
        float pickerHeight = (float) ph;
        float pickerWidth = (float) pw;
        glVertex2f(pickerX, pickerY);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glColor4f(red, green, blue, alpha);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glDisable(GL_ALPHA_TEST);
        glBegin(GL_POLYGON);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX, pickerY);
        glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glEnable(GL_ALPHA_TEST);
        glShadeModel(GL_FLAT);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }
}
