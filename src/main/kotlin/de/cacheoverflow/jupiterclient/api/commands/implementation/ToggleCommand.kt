package de.cacheoverflow.jupiterclient.api.commands.implementation

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.commands.AbstractCommand
import de.cacheoverflow.jupiterclient.api.commands.arguments.ModuleArgumentType
import de.cacheoverflow.jupiterclient.api.modules.IModuleRegistry
import de.cacheoverflow.jupiterclient.api.modules.Module
import net.minecraft.text.Text

@AbstractCommand.Declaration("commands.toggle.description", ["toggle", "t"])
class ToggleCommand(private val registry: IModuleRegistry): AbstractCommand<JupiterClient>() {

    override fun register(builder: LiteralArgumentBuilder<JupiterClient>) {
        builder
            .then(argument("module", ModuleArgumentType.module(registry)).executes { context ->
            val module: Module = ModuleArgumentType.getModule(context, "module")
            module.toggle()
            context.source.chatHelper.sendComponentWithPrefix(Text.of("The module '" + module.name + "' is now " + (if (module.enabled) "enabled" else "disabled") + "."))
            1
        }).executes { context ->
            context.source.chatHelper.sendComponentWithPrefix(Text.of("Use 'toggle <Module>'"))
            1
        }
    }

}