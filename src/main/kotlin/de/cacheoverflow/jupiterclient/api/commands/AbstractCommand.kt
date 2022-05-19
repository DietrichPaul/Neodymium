package de.cacheoverflow.jupiterclient.api.commands

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder

abstract class AbstractCommand<S: Any>: ICommand<S> {

    private val description: String
    private val aliases: Array<String>

    init {
        val declaration: Declaration = this::class.annotations.find { annotation -> Declaration::class.isInstance(annotation) } as Declaration?
            ?: throw IllegalStateException("You can't register a command without the information declaration!")
        this.description = declaration.description
        this.aliases = declaration.aliases
    }

    protected fun <E> argument(literal: String, type: ArgumentType<E>): RequiredArgumentBuilder<S, E> {
        return RequiredArgumentBuilder.argument(literal, type)
    }

    protected fun literal(literal: String): LiteralArgumentBuilder<S> {
        return LiteralArgumentBuilder.literal(literal)
    }

    override fun aliases(): Array<String> {
        return this.aliases
    }

    override fun description(): String {
        return this.description
    }

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Declaration(val description: String = "No description set!", val aliases: Array<String>)

}