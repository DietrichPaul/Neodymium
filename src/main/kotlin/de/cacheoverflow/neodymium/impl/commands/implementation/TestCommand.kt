package de.cacheoverflow.neodymium.impl.commands.implementation

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.commands.AbstractCommand
import net.minecraft.text.Text

@AbstractCommand.Declaration(aliases = ["test"])
class TestCommand: AbstractCommand<Neodymium>() {

    override fun register(builder: LiteralArgumentBuilder<Neodymium>) {
        builder.executes {
            it.source.chatHelper.sendComponentWithPrefix(Text.of("Hello World!"))
            return@executes 0
        }
    }


}