package de.cacheoverflow.jupiterclient.api.commands.arguments

import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.modules.IModuleRegistry
import de.cacheoverflow.jupiterclient.api.modules.Module
import java.util.concurrent.CompletableFuture

class ModuleArgumentType(private val registry: IModuleRegistry) : ArgumentType<Module?> {

    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): Module {
        val string: String = reader.readString()
        return registry.findFirstOr(string, true) ?: throw UNKNOWN_MODULE.createWithContext(reader, string)
    }

    override fun <S : Any?> listSuggestions(context: CommandContext<S>?, builder: SuggestionsBuilder?): CompletableFuture<Suggestions> {
        for (module in registry.asList()) {
            if (module.name.lowercase().startsWith(builder!!.remainingLowerCase)) builder.suggest(module.name)
        }
        return builder!!.buildFuture()
    }

    companion object {

        @JvmStatic val UNKNOWN_MODULE: DynamicCommandExceptionType = DynamicCommandExceptionType { name -> LiteralMessage("Unknown module '$name'") }

        @JvmStatic fun getModule(context: CommandContext<JupiterClient>, name: String): Module {
            return context.getArgument(name, Module::class.java)
        }

        @JvmStatic fun module(registry: IModuleRegistry): ModuleArgumentType {
            return ModuleArgumentType(registry)
        }

    }

}