package de.cacheoverflow.neodymium.api.ui.theme

import de.cacheoverflow.neodymium.api.ui.clickgui.IClickGuiElement
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ButtonWidget.PressAction
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.Vec2f
import kotlin.reflect.KClass

interface ITheme: StringIdentifiable {

    fun createButton(client: MinecraftClient, text: Text, description: Text, image: Identifier?, imageHeight: Int, imageWidth: Int, x: Int, y: Int, width: Int, height: Int, color: Int, onPress: PressAction): ButtonWidget

    fun createButton(client: MinecraftClient, text: Text, description: Text, x: Int, y: Int, width: Int, height: Int, color: Int, onPress: PressAction): ButtonWidget {
        return this.createButton(client, text, description, null, 0, 0, x, y, width, height, color, onPress)
    }

    fun registerClickGuiElementRenderer(type: KClass<out IClickGuiElement>, consumer: Drawable)

    fun findClickGuiElementRenderer(type: KClass<out IClickGuiElement>): Drawable

    fun isClickGuiElementRendererExisting(type: KClass<out IClickGuiElement>): Boolean

    fun renderElement(element: IClickGuiElement, matrices: MatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        if (!element.isEnabled())
            return

        this.findClickGuiElementRenderer(element::class).render(matrices, mouseX, mouseY, tickDelta)
        element.forEach { child -> this.renderElement(child, matrices, mouseX, mouseY, tickDelta) }
    }

    fun renderBackground(matrices: MatrixStack, screenBounds: Vec2f)

}