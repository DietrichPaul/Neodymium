package de.cacheoverflow.neodymium.api.commands

import com.mojang.brigadier.StringReader
import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.store.IRegistry

interface ICommandRegistry: IRegistry<ICommand<Neodymium>> {

    fun processCommand(sender: Neodymium, line: StringReader): Boolean

    fun processCommand(sender: Neodymium, line: String): Boolean {
        return this.processCommand(sender, StringReader(line))
    }

    fun setPrefix(prefix: String)

    fun getPrefix(): String

}