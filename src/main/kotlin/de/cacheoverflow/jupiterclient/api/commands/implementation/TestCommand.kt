package de.cacheoverflow.jupiterclient.api.commands.implementation

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.commands.AbstractCommand
import net.minecraft.text.Text

@AbstractCommand.Declaration(aliases = ["test"])
class TestCommand: AbstractCommand<JupiterClient>() {

    override fun register(builder: LiteralArgumentBuilder<JupiterClient>) {
        builder.executes {
            it.source.chatHelper.sendComponentWithPrefix(Text.of("Hello World!"))
            return@executes 0
        }
    }


}