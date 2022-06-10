package de.cacheoverflow.neodymium.api.utils

import com.mojang.blaze3d.systems.RenderSystem
import ladysnake.satin.api.managed.ManagedCoreShader
import ladysnake.satin.api.managed.ShaderEffectManager
import ladysnake.satin.api.managed.uniform.UniformFinder
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.Framebuffer
import net.minecraft.client.gl.SimpleFramebuffer
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.lwjgl.opengl.GL30C
import org.lwjgl.opengl.GL32C
import java.util.function.Consumer

class ShaderHelper private constructor(
        private val minecraft: MinecraftClient,
        private val effect: ManagedCoreShader,
        val useFramebuffer: Boolean
    ) {

    private var framebuffer: Framebuffer? = null

    companion object {
        @JvmStatic fun createShader(minecraft: MinecraftClient, resource: Identifier, format: VertexFormat?): ShaderHelper {
            return ShaderHelper(minecraft, if (format == null) ShaderEffectManager.getInstance().manageCoreShader(resource) else ShaderEffectManager.getInstance().manageCoreShader(resource, format), false)
        }

        @JvmStatic fun createShader(minecraft: MinecraftClient, resource: Identifier): ShaderHelper {
            return createShader(minecraft, resource, null)
        }
    }

    init {
        if (this.useFramebuffer)
            this.framebuffer = SimpleFramebuffer(this.minecraft.framebuffer.textureWidth, this.minecraft.framebuffer.textureHeight, false, true)
    }

    fun apply(prepareConsumer: Consumer<UniformFinder>, framebufferName: String, matrices: MatrixStack, x1: Float, y1: Float, x2: Float, y2: Float, color: Int) {
        this.apply(prepareConsumer, Consumer { bufferBuilder ->
            run {
                val f: Float = (color shr 24 and 255).toFloat() / 255.0f
                val g: Float = (color shr 16 and 255).toFloat() / 255.0f
                val h: Float = (color shr 8 and 255).toFloat() / 255.0f
                val j: Float = (color and 255).toFloat() / 255.0f
                bufferBuilder.vertex(matrices.peek().positionMatrix, x1, y2, 0.0f).color(g, h, j, f).next()
                bufferBuilder.vertex(matrices.peek().positionMatrix, x2, y2, 0.0f).color(g, h, j, f).next()
                bufferBuilder.vertex(matrices.peek().positionMatrix, x2, y1, 0.0f).color(g, h, j, f).next()
                bufferBuilder.vertex(matrices.peek().positionMatrix, x1, y1, 0.0f).color(g, h, j, f).next()
            }
        }, framebufferName)
    }

    fun apply(prepareConsumer: Consumer<UniformFinder>, matrices: MatrixStack, x1: Float, y1: Float, x2: Float, y2: Float, color: Int) {
        this.apply(prepareConsumer, "framebuffer", matrices, x1, y1, x2, y2, color)
    }

    fun apply(prepareConsumer: Consumer<UniformFinder>, renderConsumer: Consumer<BufferBuilder>, framebufferName: String) {
        val bufferBuilder: BufferBuilder = Tessellator.getInstance().buffer
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()

        if (this.framebuffer != null && this.framebuffer?.textureWidth == this.minecraft.framebuffer.textureWidth || this.framebuffer?.textureHeight == this.minecraft.framebuffer.textureHeight) {
            this.framebuffer?.resize(this.minecraft.framebuffer.textureWidth, this.minecraft.framebuffer.textureHeight, true)
            this.framebuffer?.setClearColor(0f, 0f, 0f, 0f)
            this.framebuffer?.clear(true)

            GL32C.glBindFramebuffer(GL30C.GL_DRAW_FRAMEBUFFER, this.framebuffer?.fbo!!)
            GL32C.glBindFramebuffer(GL30C.GL_READ_FRAMEBUFFER, this.framebuffer?.fbo!!)
            GL32C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, this.framebuffer?.fbo!!)

            GL32C.glBlitFramebuffer(0, 0, this.framebuffer?.textureWidth!!, this.framebuffer?.textureHeight!!,
                0, 0, this.framebuffer?.textureWidth!!, this.framebuffer?.textureHeight!!, GL32C.GL_COLOR_BUFFER_BIT, GL32C.GL_NEAREST)
            this.minecraft.framebuffer.beginWrite(false)

            this.effect.findSampler(framebufferName).set(this.framebuffer)
        }

        prepareConsumer.accept(this.effect)

        RenderSystem.setShader(this.effect::getProgram)
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        renderConsumer.accept(bufferBuilder)
        bufferBuilder.end()
        BufferRenderer.draw(bufferBuilder)

        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
    }

    fun apply(prepareConsumer: Consumer<UniformFinder>, renderConsumer: Consumer<BufferBuilder>) {
        this.apply(prepareConsumer, renderConsumer, "framebuffer")
    }

}