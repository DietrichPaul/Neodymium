package de.cacheoverflow.jupiterclient.impl.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.tree.LiteralCommandNode
import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.commands.ICommand
import de.cacheoverflow.jupiterclient.api.commands.ICommandRegistry
import de.cacheoverflow.jupiterclient.api.store.IRegistry
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting
import java.util.function.Function
import java.util.function.Predicate

class DefaultCommandRegistry(
    private val elements: MutableCollection<ICommand<JupiterClient>> = ArrayList(),
    private var unregisterPredicate: Predicate<ICommand<JupiterClient>>? = Predicate { true },
    private var prefix: String = "-"
): ICommandRegistry<JupiterClient> {

    private val commandDispatcher: CommandDispatcher<JupiterClient> = CommandDispatcher()
    private val registerPredicate: Predicate<ICommand<JupiterClient>> = Predicate {
        val builder: LiteralArgumentBuilder<JupiterClient> = LiteralArgumentBuilder.literal(it.aliases()[0])
        it.register(builder)
        val node: LiteralCommandNode<JupiterClient> = this.commandDispatcher.register(builder)

        for (i in 1 until it.aliases().size) {
            this.commandDispatcher.register(LiteralArgumentBuilder.literal<JupiterClient>(it.aliases()[i]).redirect(node))
        }

        return@Predicate true
    }

    override fun processCommand(sender: JupiterClient, line: StringReader): Boolean {
        if (!line.string.startsWith(this.prefix))
            return true

        line.cursor = this.prefix.length
        try {
            this.commandDispatcher.execute(line, sender)
        } catch (ex: CommandSyntaxException) {
            sender.chatHelper.sendComponentWithPrefix(LiteralText(ex.message).formatted(Formatting.RED))
        }
        return false
    }

    override fun setPrefix(prefix: String) {
        this.prefix = prefix
    }

    override fun getPrefix(): String {
        return this.prefix
    }

    override fun getRegisterPredicate(): Predicate<ICommand<JupiterClient>> {
        return this.registerPredicate
    }

    override fun getUnregisterPredicate(): Predicate<ICommand<JupiterClient>>? {
        return this.unregisterPredicate
    }

    override fun asList(): MutableCollection<ICommand<JupiterClient>> {
        return ArrayList(this.elements)
    }

    override fun setUnregisterPredicate(predicate: Predicate<ICommand<JupiterClient>>?) {
        this.unregisterPredicate = predicate
    }

    override fun setRegisterPredicate(predicate: Predicate<ICommand<JupiterClient>>?) {
        throw UnsupportedOperationException()
    }

    override fun <E> directAction(action: Function<MutableCollection<ICommand<JupiterClient>>, E?>): E? {
        return action.apply(this.elements)
    }

    override fun iterator(): Iterator<ICommand<JupiterClient>> {
        return this.elements.iterator()
    }

    override fun copy(): IRegistry<ICommand<JupiterClient>> {
        return DefaultCommandRegistry(ArrayList(this.elements))
    }

}