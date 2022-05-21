package de.cacheoverflow.jupiterclient.api.modules

import de.cacheoverflow.jupiterclient.JupiterClient
import net.minecraft.util.StringIdentifiable
import kotlin.reflect.full.findAnnotation

open class Module(val client: JupiterClient): StringIdentifiable {

    val name: String
    val description: String
    val category: EnumCategory
    val listenable: Boolean

    var enabled: Boolean = false

    init {
        val declaration = this::class.findAnnotation<Declaration>() ?: throw IllegalStateException("")
        this.name = declaration.name
        this.description = declaration.description
        this.category = declaration.category
        this.listenable = declaration.listenable
        if (declaration.enabled)
            this.toggle()
    }

    fun toggle() {
        this.enabled = !this.enabled
        if (this.enabled) {
            if (this.listenable)
                this.client.eventBus.registerListeners(arrayOf(this))
            this.onEnable()
        } else {
            if (this.listenable)
                this.client.eventBus.unregisterListeners(arrayOf(this))
            this.onDisable()
        }
    }

    override fun asString(): String {
        return this.name
    }

    protected open fun onEnable() {}

    protected open fun onDisable() {}

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Declaration(
        val name: String,
        val description: String = "This is the default description.",
        val category: EnumCategory,
        val enabled: Boolean = false,
        val listenable: Boolean = false
    )

    enum class EnumCategory(name: String) {
        COMBAT("Combat"), MOVEMENT("Movement"), PLAYER("Player"), WORLD("World"), VISUAL("Visual"), GUI("GUI");

        override fun toString(): String {
            return name
        }
    }

}