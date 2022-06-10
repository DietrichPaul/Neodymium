package de.cacheoverflow.neodymium.api.utils

import de.cacheoverflow.neodymium.Neodymium
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting

interface IChatHelper {

    fun sendComponent(text: Text, withPrefix: Boolean)

    fun sendComponentWithPrefix(text: Text) {
        this.sendComponent(text, true)
    }

    fun sendComponent(text: String) {
        this.sendComponent(LiteralText(text), false)
    }

    fun sendMessage(code: String, withPrefix: Boolean, vararg replacements: Any) {
        this.sendComponent(TranslatableText(code, *replacements), withPrefix)
    }

    fun sendMessageWithPrefix(code: String, vararg replacements: Any) {
        sendMessage(code, true, *replacements)
    }

    fun sendMessageWithoutPrefix(code: String, vararg replacements: Any) {
        sendMessage(code, false, *replacements)
    }

    val PREFIX: MutableText
        get() = LiteralText("")
            .append(LiteralText("[").formatted(Formatting.DARK_GRAY))
            .append(LiteralText(Neodymium::class.java.simpleName).formatted(Formatting.AQUA))
            .append(LiteralText("] ").formatted(Formatting.DARK_GRAY))

}