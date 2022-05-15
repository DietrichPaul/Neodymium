package de.cacheoverflow.jupiterclient.api.utils

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.math.Vector4f

class RenderHelper private constructor() {

    init {
        throw IllegalStateException("This class is not intended to be instantiated!")
    }

    companion object {
        @JvmStatic
        fun fillBorder(
            matrices: MatrixStack, left: Int, top: Int, right: Int,
            bottom: Int, color: Int, size: Int
        ) {
            DrawableHelper.fill(matrices, left - size, top - size, left, bottom + size, color)
            DrawableHelper.fill(matrices, right, top - size, right + size, bottom + size, color)
            DrawableHelper.fill(matrices, left, top - size, right, top, color)
            DrawableHelper.fill(matrices, left, bottom, right, bottom + size, color)
        }

        @JvmStatic fun scissor(client: MinecraftClient, matrices: MatrixStack, left: Int, top: Int, right: Int, bottom: Int) {
            val modelView = matrices.peek().positionMatrix
            val leftTopVec = Vector4f(left.toFloat(), top.toFloat(), 0f, 1f)
            leftTopVec.transform(modelView)
            val rightBottomVec = Vector4f(right.toFloat(), bottom.toFloat(), 0f, 1f)
            rightBottomVec.transform(modelView)
            val projectionMatrix = RenderSystem.getProjectionMatrix()
            val width = rightBottomVec.x - leftTopVec.x
            val height = rightBottomVec.y - leftTopVec.y
            val start = client.window.scaledHeight - rightBottomVec.y
            val window = client.window
            val transformedStart = projectOrtho2D(
                leftTopVec.x,
                start,
                0f,
                window.framebufferWidth.toFloat(),
                window.framebufferHeight.toFloat(),
                projectionMatrix
            )
            val transformedDimension = projectOrtho2D(
                width,
                height,
                0f,
                window.framebufferWidth.toFloat(),
                window.framebufferHeight.toFloat(),
                projectionMatrix
            )
            GlStateManager._scissorBox(
                MathHelper.ceil(transformedStart[0] - 0.5f),
                MathHelper.ceil(transformedStart[1] - 0.5f),
                MathHelper.ceil(transformedDimension[0] - 0.5f),
                MathHelper.ceil(transformedDimension[1] - 0.5f)
            )
        }

        @JvmStatic fun projectOrtho2D(x: Float, y: Float, z: Float, width: Float, height: Float, matrix: Matrix4f?): FloatArray {
            var x1 = x
            var y1 = y
            val vector = Vector4f(x, y, z, 1.0f)
            vector.transform(matrix)
            x1 = (vector.x + 1.0f) / 2.0f
            y1 = (-vector.y + 1.0f) / 2.0f
            return floatArrayOf(x1 * width, y1 * height)
        }
    }
}