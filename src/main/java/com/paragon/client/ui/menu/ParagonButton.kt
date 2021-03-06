package com.paragon.client.ui.menu


import com.paragon.api.util.render.RenderUtil
import com.paragon.api.util.render.font.FontUtil
import com.paragon.client.systems.module.impl.client.Colours
import com.paragon.client.ui.util.animation.Animation
import com.paragon.client.ui.util.animation.Easing
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.math.MathHelper
import java.awt.Color

/**
 * @author Surge
 */
class ParagonButton(buttonId: Int, x: Int, y: Int, widthIn: Int, heightIn: Int, buttonText: String) : GuiButton(buttonId, x, y, widthIn, heightIn, buttonText) {

    private val animation = Animation({ 300.0f }, false, { Easing.EXPO_IN_OUT })

    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (visible) {
            animation.state = hovered

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height

            GlStateManager.enableBlend()

            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)

            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)

            RenderUtil.drawRect(
                x.toFloat(),
                y.toFloat(),
                width.toFloat(),
                height.toFloat(),
                if (hovered) -0x70000000 else -0x80000000
            )

            RenderUtil.drawRect(x + (width / 2f - width / 2f * animation.getAnimationFactor().toFloat()), (y + height - 1).toFloat(), (width * animation.getAnimationFactor()).toFloat(), 1f, Colours.mainColour.value.rgb)

            mouseDragged(mc, mouseX, mouseY)

            FontUtil.renderCenteredString(displayString, x + width / 2f, y + (height / 2f) + 1.5f, 0xFFFFFF, true)
        }
    }

}