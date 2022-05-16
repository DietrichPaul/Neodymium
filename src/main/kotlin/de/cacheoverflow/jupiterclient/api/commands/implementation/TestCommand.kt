package de.cacheoverflow.jupiterclient.api.commands.implementation

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.commands.AbstractCommand

@AbstractCommand.Declaration(aliases = ["test"])
class TestCommand: AbstractCommand<JupiterClient>() {

    override fun register(builder: LiteralArgumentBuilder<JupiterClient>) {
        builder.executes {
            println("This is a test command!")
            return@executes 0
        }
    }


}