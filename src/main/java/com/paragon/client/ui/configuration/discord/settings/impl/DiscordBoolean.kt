package com.paragon.client.ui.configuration.discord.settings.impl

import com.paragon.api.setting.Setting
import com.paragon.api.util.render.font.FontUtil

import com.paragon.client.systems.module.impl.client.Colours
import com.paragon.client.ui.configuration.discord.settings.DiscordSetting
import com.paragon.client.ui.util.Click
import org.lwjgl.util.Rectangle

/**
 * @author SooStrator1136
 */
class DiscordBoolean(val setting: Setting<Boolean>) : DiscordSetting(setting) {

    private val stateRect = Rectangle()

    init {
        bounds.height = (FontUtil.getHeight() + msgStyleHeight).toInt() + 2
    }

    override fun render(mouseX: Int, mouseY: Int) {
        super.render(mouseX, mouseY)

        stateRect.setBounds(
            bounds.x,
            (bounds.y + FontUtil.getHeight() + 1).toInt(),
            FontUtil.getStringWidth(setting.value.toString()).toInt(),
            FontUtil.getHeight().toInt()
        )

        FontUtil.drawStringWithShadow(
            setting.value.toString(),
            bounds.x.toFloat(),
            bounds.y + FontUtil.getHeight() + 1F,
            if (stateRect.contains(mouseX, mouseY)) Colours.mainColour.value.rgb else -1
        )
    }

    override fun onClick(mouseX: Int, mouseY: Int, button: Int) {
        if (button != Click.LEFT.button) {
            return
        }
        if (stateRect.contains(mouseX, mouseY)) {
            setting.setValue(!setting.value)
        }
    }

    override fun onKey(keyCode: Int) {}

}