package de.cacheoverflow.jupiterclient.api.commands

import com.mojang.brigadier.StringReader
import de.cacheoverflow.jupiterclient.api.store.IRegistry

interface ICommandRegistry<S: Any>: IRegistry<ICommand<S>> {

    fun processCommand(sender: S, line: StringReader): Boolean

    fun processCommand(sender: S, line: String): Boolean {
        return this.processCommand(sender, StringReader(line))
    }

    fun setPrefix(prefix: String)

    fun getPrefix(): String

}