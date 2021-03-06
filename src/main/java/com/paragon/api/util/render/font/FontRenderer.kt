package com.paragon.api.util.render.font

import com.paragon.api.util.Wrapper
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ChatAllowedCharacters
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import java.awt.Font
import java.util.*

/**
 * @author Cosmos, Surge
 */
@SideOnly(Side.CLIENT)
class FontRenderer(font: Font) : Wrapper {
    private val fontHeight: Int
    private val defaultFont: ImageAWT

    init {
        defaultFont = ImageAWT(font)
        fontHeight = height.toInt()
    }

    val height: Float
        get() = defaultFont.height / 2f

    val size: Int
        get() = defaultFont.font.size

    fun drawStringWithShadow(text: String, x: Float, y: Float, color: Int): Int {
        return drawString(text, x, y, color, true)
    }

    fun drawString(text: String, x: Float, y: Float, color: Int, dropShadow: Boolean): Int {
        if (text.contains("\n")) {
            val parts = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var newY = 0.0f

            for (s in parts) {
                if (dropShadow) {
                    drawText(s, x + 0.6f, y + newY + 0.6f, Color(0, 0, 0, 150).rgb, true)
                }

                drawText(s, x, y + newY, color, dropShadow)
                newY += height
            }

            return 0
        }

        if (dropShadow) {
            drawText(text, x + 0.6f, y + 0.6f, Color(0, 0, 0, 150).rgb, true)
        }

        return drawText(text, x, y, color, false)
    }

    private fun drawText(text: String?, x: Float, y: Float, color: Int, ignoreColor: Boolean): Int {
        if (text == null) {
            return 0
        }

        if (text.isEmpty()) {
            return x.toInt()
        }

        GlStateManager.translate(x.toDouble(), y.toDouble(), 0.0)
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.enableTexture2D()
        glEnable(GL_LINE_SMOOTH)

        var currentColor = color
        if (currentColor and -0x4000000 == 0) {
            currentColor = currentColor or -0x1000000
        }

        val alpha = currentColor shr 24 and 0xFF

        if (text.contains("??")) {
            val parts = text.split("??".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val currentFont = defaultFont
            var width = 0.0
            var randomCase = false

            for (index in parts.indices) {
                val part = parts[index]

                if (part.isEmpty()) {
                    continue
                }

                if (index == 0) {
                    currentFont.drawString(part, width, 0.0, currentColor)
                    width += currentFont.getStringWidth(part).toDouble()
                    continue
                }

                val words = part.substring(1)
                val type = part[0]

                when (val colorIndex = "0123456789abcdefklmnor".indexOf(type)) {
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 -> {
                        if (!ignoreColor) currentColor = ColorUtils.hexColors[colorIndex] or (alpha shl 24)
                        randomCase = false
                    }

                    16 -> {
                        randomCase = true
                    }

                    18 -> {}

                    21 -> {
                        currentColor = color

                        if (currentColor and -0x4000000 == 0) {
                            currentColor = currentColor or -0x1000000
                        }

                        randomCase = false
                    }
                }

                if (randomCase) {
                    currentFont.drawString(ColorUtils.randomMagicText(words), width, 0.0, currentColor)
                } else {
                    currentFont.drawString(words, width, 0.0, currentColor)
                }

                width += currentFont.getStringWidth(words).toDouble()
            }
        } else {
            defaultFont.drawString(text, 0.0, 0.0, currentColor)
        }

        glDisable(GL_LINE_SMOOTH)
        GlStateManager.disableBlend()
        GlStateManager.translate(-x.toDouble(), -y.toDouble(), 0.0)

        return (x + getStringWidth(text).toFloat()).toInt()
    }

    fun getStringWidth(text: String): Int {
        if (text.contains("??")) {
            val parts = text.split("??".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val currentFont = defaultFont
            var width = 0

            for (index in parts.indices) {
                val part = parts[index]

                if (part.isEmpty()) {
                    continue
                }

                if (index == 0) {
                    width += currentFont.getStringWidth(part)
                    continue
                }

                val words = part.substring(1)
                width += currentFont.getStringWidth(words)
            }

            return width / 2
        }

        return defaultFont.getStringWidth(text) / 2
    }

    private object ColorUtils {
        private var random: Random? = null

        private const val magicAllowedCharacters = "?????????????????????????????????????????????????? !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????"

        var hexColors = IntArray(16)

        init {
            hexColors[0] = 0
            hexColors[1] = 170
            hexColors[2] = 43520
            hexColors[3] = 43690
            hexColors[4] = 0xAA0000
            hexColors[5] = 0xAA00AA
            hexColors[6] = 0xFFAA00
            hexColors[7] = 0xAAAAAA
            hexColors[8] = 0x555555
            hexColors[9] = 0x5555FF
            hexColors[10] = 0x55FF55
            hexColors[11] = 0x55FFFF
            hexColors[12] = 0xFF5555
            hexColors[13] = 0xFF55FF
            hexColors[14] = 0xFFFF55
            hexColors[15] = 0xFFFFFF

            random = Random()
        }

        fun randomMagicText(text: String): String {
            val stringBuilder = StringBuilder()

            for (ch in text.toCharArray()) {
                if (!ChatAllowedCharacters.isAllowedCharacter(ch)) {
                    continue
                }

                val index = random!!.nextInt(magicAllowedCharacters.length)
                stringBuilder.append(magicAllowedCharacters[index])
            }

            return stringBuilder.toString()
        }
    }

    companion object {
        fun getColorIndex(type: Char): Int {
            when (type) {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> return type.code - 48

                'a', 'b', 'c', 'd', 'e', 'f' -> return type.code - 97 + 10

                'k', 'l', 'm', 'n', 'o' -> return type.code - 107 + 16

                'r' -> return 21
            }

            return -1
        }
    }
}