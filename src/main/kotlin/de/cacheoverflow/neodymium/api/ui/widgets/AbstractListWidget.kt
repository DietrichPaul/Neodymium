package de.cacheoverflow.neodymium.api.ui.widgets

import com.mojang.blaze3d.platform.GlStateManager
import de.cacheoverflow.neodymium.api.utils.ColorHelper.Companion.changeAlpha
import de.cacheoverflow.neodymium.api.utils.RenderHelper.Companion.fillBorder
import de.cacheoverflow.neodymium.api.utils.RenderHelper.Companion.scissor
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.glfw.GLFW
import java.awt.Color
import java.util.function.IntFunction
import java.util.function.IntSupplier

abstract class AbstractListWidget<T>(
    private val client: MinecraftClient, val elementSupplier: IntFunction<T>,
    val sizeSupplier: IntSupplier, var left: Int, var top: Int, var right: Int,
    var bottom: Int, val entryOffset: Int, var startY: Int, var stopY: Int
) : Drawable {

    private var prevY = 0
    private var scrolled = 0
    var lastClick: Long = 0

    abstract fun renderSlot(
        matrices: MatrixStack,
        element: T,
        index: Int,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    )

    abstract fun getHeight(index: Int): Int
    abstract fun click(index: Int, mouseX: Double, mouseY: Double, button: Int, doubleClick: Boolean)
    abstract var selected: Int
    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        var y = top + startY + scrolled + 1
        GlStateManager._enableScissorTest()
        scissor(client, matrices, left, top + startY, right, bottom - stopY)
        for (i in 0 until sizeSupplier.asInt) {
            val element = elementSupplier.apply(i)
            val height = getHeight(i)
            DrawableHelper.fill(matrices, left, y, right, y + height, changeAlpha(Color.BLACK.rgb, 0.5f))
            if (isSelected(i)) fillBorder(matrices, left, y, right, y + height, Color.WHITE.rgb, 1)
            renderSlot(matrices, element, i, left, y, right - left, height, mouseX, mouseY, delta)
            y += height + entryOffset
        }
        prevY = y
        GlStateManager._disableScissorTest()
    }

    fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (!isInBox(mouseX, mouseY) || amount < 0 && prevY + entryOffset < stopY) return false
        scrolled -= (amount * (entryOffset * 2)).toInt()
        return true
    }

    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        var y = top + startY + scrolled
        for (i in 0 until sizeSupplier.asInt) {
            val height = getHeight(i)
            if (mouseY > y && mouseY < y + height) {
                click(i, mouseX, mouseY, button, System.currentTimeMillis() - lastClick < 200)
                lastClick = System.currentTimeMillis()
                return true
            }
            y += height + entryOffset
        }
        return true
    }

    fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_UP) selected -= 1 else if (keyCode == GLFW.GLFW_KEY_DOWN) selected += 1
        return true
    }

    fun isInBox(mouseX: Double, mouseY: Double): Boolean {
        return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom
    }

    fun isSelected(index: Int): Boolean {
        return index == selected
    }
}