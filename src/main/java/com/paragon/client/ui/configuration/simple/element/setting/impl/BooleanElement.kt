package com.paragon.client.ui.configuration.simple.element.setting.impl

import com.paragon.api.setting.Setting
import com.paragon.api.util.render.ColourUtil
import com.paragon.api.util.render.RenderUtil
import com.paragon.api.util.render.font.FontUtil
import com.paragon.client.systems.module.impl.client.Colours
import com.paragon.client.ui.configuration.simple.element.setting.SettingElement
import com.paragon.client.ui.util.Click
import org.lwjgl.opengl.GL11.glScalef
import java.awt.Color

/**
 * @author Surge
 * @since 31/07/2022
 */
class BooleanElement(setting: Setting<Boolean>, x: Float, y: Float, width: Float, height: Float) : SettingElement<Boolean>(setting, x, y, width, height) {

    override fun draw(mouseX: Float, mouseY: Float, mouseDelta: Int) {
        val hovered = isHovered(mouseX, mouseY)

        if (hovered && !setting.value) {
            RenderUtil.drawRect(x, y, width, height, Color(200, 200, 200, 150).rgb)
        }

        if (setting.value) {
            RenderUtil.drawRect(x, y, width, height, ColourUtil.integrateAlpha(Colours.mainColour.value, if (hovered) 205f else 150f).rgb)
        }

        glScalef(0.85f, 0.85f, 0.85f).let {
            val factor = 1 / 0.85f

            FontUtil.drawStringWithShadow(setting.name, (x + 4) * factor, (y + 4) * factor, -1)

            glScalef(factor, factor, factor)
        }

        super.draw(mouseX, mouseY, mouseDelta)
    }

    override fun mouseClicked(mouseX: Float, mouseY: Float, click: Click) {
        super.mouseClicked(mouseX, mouseY, click)

        if (isHovered(mouseX, mouseY) && click == Click.LEFT) {
            setting.setValue(!setting.value)
        }
    }

}