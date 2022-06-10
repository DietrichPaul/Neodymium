package de.cacheoverflow.neodymium.api.ui.widgets

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.Drawable
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

class TextWidget(
    private val textRenderer: TextRenderer,
    private val text: Text,
    private val x: Int,
    private val y: Int,
    private val color: Int
) : Drawable {

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        textRenderer.draw(matrices, text, x.toFloat(), y.toFloat(), color)
    }

}