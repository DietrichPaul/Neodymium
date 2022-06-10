package de.cacheoverflow.neodymium.api.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.math.Vector4f
import org.lwjgl.opengl.GL20C

class RenderHelper private constructor() {

    init {
        throw IllegalStateException("This class is not intended to be instantiated!")
    }

    companion object {

        @JvmStatic fun scissor(client: MinecraftClient, matrices: MatrixStack, left: Int, top: Int, right: Int, bottom: Int) {
            val modelViewMatrix: Matrix4f = matrices.peek().positionMatrix

            val leftTopVec = Vector4f(left.toFloat(), top.toFloat(), 0f, 0f)
            leftTopVec.transform(modelViewMatrix)

            val rightBottomVec = Vector4f(right.toFloat(), bottom.toFloat(), 0f, 0f)
            rightBottomVec.transform(modelViewMatrix)

            val projectionMatrix: Matrix4f = RenderSystem.getProjectionMatrix()

            val width: Float = rightBottomVec.x - leftTopVec.x
            val height: Float = rightBottomVec.y - leftTopVec.y
            val start: Float = client.window.scaledHeight - rightBottomVec.y

            val transformedStart: FloatArray = projectOrthographical2D(projectionMatrix, leftTopVec.x, start, 0f, client.window.framebufferWidth.toFloat(), client.window.framebufferHeight.toFloat())
            val transformedDimension: FloatArray = projectOrthographical2D(projectionMatrix, width, height, 0f, client.window.framebufferWidth.toFloat(), client.window.framebufferHeight.toFloat())

            GL20C.glScissor(
                MathHelper.ceil(transformedStart[0] - 0.5f),
                MathHelper.ceil(transformedStart[1] - 0.5f),
                MathHelper.ceil(transformedDimension[0] - 0.5f),
                MathHelper.ceil(transformedDimension[1] - 0.5f)
            )
        }

        @JvmStatic fun projectOrthographical2D(matrix: Matrix4f, x: Float, y: Float, z: Float, width: Float, height: Float): FloatArray {
            val vector = Vector4f(x, y, z, 1.0f)
            vector.transform(matrix)
            return floatArrayOf(((vector.x + 1.0F) / 2.0F) * width, ((vector.x + 1.0F) / 2.0F) * height)
        }

        @JvmStatic fun fillBorder(
            matrices: MatrixStack, left: Int, top: Int, right: Int,
            bottom: Int, color: Int, size: Int
        ) {
            DrawableHelper.fill(matrices, left - size, top - size, left, bottom + size, color)
            DrawableHelper.fill(matrices, right, top - size, right + size, bottom + size, color)
            DrawableHelper.fill(matrices, left, top - size, right, top, color)
            DrawableHelper.fill(matrices, left, bottom, right, bottom + size, color)
        }

    }
}