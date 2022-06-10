package de.cacheoverflow.neodymium.impl.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.tree.LiteralCommandNode
import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.commands.ICommand
import de.cacheoverflow.neodymium.api.commands.ICommandRegistry
import de.cacheoverflow.neodymium.api.store.IRegistry
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting
import java.util.function.Function
import java.util.function.Predicate

class DefaultCommandRegistry(
    private val elements: MutableCollection<ICommand<Neodymium>> = ArrayList(),
    private var unregisterPredicate: Predicate<ICommand<Neodymium>>? = Predicate { true },
    private var prefix: String = "-"
): ICommandRegistry {

    private val commandDispatcher: CommandDispatcher<Neodymium> = CommandDispatcher()
    private val registerPredicate: Predicate<ICommand<Neodymium>> = Predicate {
        val builder: LiteralArgumentBuilder<Neodymium> = LiteralArgumentBuilder.literal(it.aliases()[0])
        it.register(builder)
        val node: LiteralCommandNode<Neodymium> = this.commandDispatcher.register(builder)

        for (i in 1 until it.aliases().size) {
            this.commandDispatcher.register(LiteralArgumentBuilder.literal<Neodymium>(it.aliases()[i]).redirect(node))
        }

        return@Predicate true
    }

    override fun processCommand(sender: Neodymium, line: StringReader): Boolean {
        if (!line.string.startsWith(this.prefix))
            return true

        line.cursor = this.prefix.length
        try {
            println(line.remaining)
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

    override fun getRegisterPredicate(): Predicate<ICommand<Neodymium>> {
        return this.registerPredicate
    }

    override fun getUnregisterPredicate(): Predicate<ICommand<Neodymium>>? {
        return this.unregisterPredicate
    }

    override fun asList(): MutableCollection<ICommand<Neodymium>> {
        return ArrayList(this.elements)
    }

    override fun setUnregisterPredicate(predicate: Predicate<ICommand<Neodymium>>?) {
        this.unregisterPredicate = predicate
    }

    override fun setRegisterPredicate(predicate: Predicate<ICommand<Neodymium>>?) {
        throw UnsupportedOperationException()
    }

    override fun <E> directAction(action: Function<MutableCollection<ICommand<Neodymium>>, E?>): E? {
        return action.apply(this.elements)
    }

    override fun iterator(): Iterator<ICommand<Neodymium>> {
        return this.elements.iterator()
    }

    override fun copy(): IRegistry<ICommand<Neodymium>> {
        return DefaultCommandRegistry(ArrayList(this.elements), registerPredicate, prefix)
    }

}