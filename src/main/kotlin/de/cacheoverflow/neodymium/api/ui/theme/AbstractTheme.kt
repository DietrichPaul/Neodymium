package de.cacheoverflow.neodymium.api.ui.theme

import de.cacheoverflow.neodymium.api.ui.clickgui.IClickGuiElement
import net.minecraft.client.gui.Drawable
import kotlin.reflect.KClass

abstract class AbstractTheme(private val literal: String): ITheme {

    private val elementRendererMap: MutableMap<KClass<out IClickGuiElement>, Drawable> = HashMap()

    override fun registerClickGuiElementRenderer(type: KClass<out IClickGuiElement>, consumer: Drawable) {
        if (this.isClickGuiElementRendererExisting(type))
            throw IllegalStateException("The ClickGUI element renderer " + type.simpleName + " is already registered!")

        this.elementRendererMap[type] = consumer
    }

    override fun findClickGuiElementRenderer(type: KClass<out IClickGuiElement>): Drawable {
        if (!this.isClickGuiElementRendererExisting(type))
            throw NullPointerException("The ClickGUI element renderer " + type.simpleName + " is not existing!")

        return this.elementRendererMap[type]!!
    }

    override fun isClickGuiElementRendererExisting(type: KClass<out IClickGuiElement>): Boolean {
        return this.elementRendererMap[type] != null
    }

    override fun asString(): String {
        return this.literal
    }
}