package de.cacheoverflow.jupiterclient.impl.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.tree.LiteralCommandNode
import de.cacheoverflow.jupiterclient.api.commands.ICommand
import de.cacheoverflow.jupiterclient.api.commands.ICommandRegistry
import de.cacheoverflow.jupiterclient.api.store.IRegistry
import java.util.function.Function
import java.util.function.Predicate

class DefaultCommandRegistry<S: Any>(
    private val elements: MutableCollection<ICommand<S>> = ArrayList(),
    private var unregisterPredicate: Predicate<ICommand<S>>? = Predicate { true },
    private var prefix: String = "-"
): ICommandRegistry<S> {

    private val commandDispatcher: CommandDispatcher<S> = CommandDispatcher()
    private val registerPredicate: Predicate<ICommand<S>> = Predicate {
        val builder: LiteralArgumentBuilder<S> = LiteralArgumentBuilder.literal(it.aliases()[0])
        it.register(builder)
        val node: LiteralCommandNode<S> = this.commandDispatcher.register(builder)

        for (i in 1 until it.aliases().size) {
            this.commandDispatcher.register(LiteralArgumentBuilder.literal<S>(it.aliases()[i]).redirect(node))
        }

        return@Predicate true
    }

    override fun processCommand(sender: S, line: StringReader): Boolean {
        if (!line.string.startsWith(this.prefix))
            return true

        line.cursor = this.prefix.length
        try {
            this.commandDispatcher.execute(line, sender)
        } catch (ex: CommandSyntaxException) {
            TODO("Print exception!")
        }
        return false
    }

    override fun setPrefix(prefix: String) {
        this.prefix = prefix
    }

    override fun getPrefix(): String {
        return this.prefix
    }

    override fun getRegisterPredicate(): Predicate<ICommand<S>>? {
        return this.registerPredicate
    }

    override fun getUnregisterPredicate(): Predicate<ICommand<S>>? {
        return this.unregisterPredicate
    }

    override fun asList(): MutableCollection<ICommand<S>> {
        return ArrayList(this.elements)
    }

    override fun setUnregisterPredicate(predicate: Predicate<ICommand<S>>?) {
        this.unregisterPredicate = predicate
    }

    override fun setRegisterPredicate(predicate: Predicate<ICommand<S>>?) {
        throw UnsupportedOperationException()
    }

    override fun <E> directAction(action: Function<MutableCollection<ICommand<S>>, E?>): E? {
        return action.apply(this.elements)
    }

    override fun iterator(): Iterator<ICommand<S>> {
        return this.elements.iterator()
    }

    override fun copy(): IRegistry<ICommand<S>> {
        return DefaultCommandRegistry(ArrayList(this.elements))
    }

}