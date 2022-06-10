package de.cacheoverflow.neodymium.api.ui.clickgui

import de.cacheoverflow.neodymium.api.store.IRegistry
import net.minecraft.client.gui.Element
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec2f

interface IClickGuiElement: IRegistry<IClickGuiElement>, StringIdentifiable, Element {

    fun getBoundingBox(): Box {
        val endPosition: Vec2f = this.getPosition().add(this.getElementSize())
        return Box(this.getPosition().x.toDouble(), this.getPosition().y.toDouble(), 0.0, endPosition.x.toDouble(), endPosition.y.toDouble(), 0.0)
    }

    fun isHovering(mouseX: Int, mouseY: Int): Boolean {
        return this.getBoundingBox().contains(mouseX.toDouble(), mouseY.toDouble(), 1.0)
    }

    fun setPosition(position: Vec2f)

    fun getPosition(): Vec2f

    fun setElementSize(bounds: Vec2f)

    fun getElementSize(): Vec2f

    fun setEnabled(enabled: Boolean)

    fun isEnabled(): Boolean

}