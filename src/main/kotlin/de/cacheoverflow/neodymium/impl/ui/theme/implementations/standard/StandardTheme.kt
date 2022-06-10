package de.cacheoverflow.neodymium.impl.ui.theme.implementations.standard

import com.mojang.blaze3d.systems.RenderSystem
import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.ui.theme.AbstractTheme
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f
import java.util.*

class StandardTheme: AbstractTheme("Standard") {

    private val DARK_BACKGROUND = Identifier(Neodymium::class.java.simpleName.lowercase(Locale.getDefault()), "textures/backgrounds/dark.png")

    override fun createButton(client: MinecraftClient, text: Text, description: Text, image: Identifier?,
                              imageHeight: Int, imageWidth: Int, x: Int, y: Int, width: Int, height: Int, color: Int,
                              onPress: ButtonWidget.PressAction): ButtonWidget {
        return StandardButtonWidget(client, text, description, image!!, imageHeight, imageWidth, x, y, width, height, color, onPress)
    }

    override fun renderBackground(matrices: MatrixStack, screenBounds: Vec2f) {
        RenderSystem.setShaderTexture(0, DARK_BACKGROUND)
        DrawableHelper.drawTexture(matrices, 0, 0, 0f, 0f, screenBounds.x.toInt(), screenBounds.y.toInt(), screenBounds.x.toInt(), screenBounds.y.toInt())
    }

}