package de.cacheoverflow.neodymium.api.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder

interface ICommand<S: Any> {

    fun register(builder: LiteralArgumentBuilder<S>)

    fun aliases(): Array<String>

    fun description(): String

}