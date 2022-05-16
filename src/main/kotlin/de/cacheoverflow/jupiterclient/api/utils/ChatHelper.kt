package de.cacheoverflow.jupiterclient.api.utils

import com.google.common.base.Preconditions
import de.cacheoverflow.jupiterclient.JupiterClient
import net.minecraft.client.MinecraftClient
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting

class ChatHelper(client: MinecraftClient) {
    private val client: MinecraftClient

    init {
        Preconditions.checkNotNull(client, "The client cannot be null!")
        this.client = client
    }

    fun sendComponent(text: Text, withPrefix: Boolean) {
        assert(client.player != null)
        client.player!!.sendMessage(
            (if (withPrefix) PREFIX.shallowCopy() else LiteralText("")).append(text).formatted(
                Formatting.GRAY
            ), false
        )
    }

    fun sendComponentWithPrefix(text: Text) {
        this.sendComponent(text, true)
    }

    fun sendComponent(text: String) {
        this.sendComponent(LiteralText(text), false)
    }

    fun sendMessage(code: String, withPrefix: Boolean, vararg replacements: Any?) {
        this.sendComponent(TranslatableText(code, *replacements), withPrefix)
    }

    fun sendMessageWithPrefix(code: String, vararg replacements: Any?) {
        sendMessage(code, true, *replacements)
    }

    fun sendMessageWithoutPrefix(code: String, vararg replacements: Any?) {
        sendMessage(code, false, *replacements)
    }

    companion object {
        @JvmStatic val PREFIX = LiteralText("")
            .append(LiteralText("[").formatted(Formatting.DARK_GRAY))
            .append(LiteralText(JupiterClient::class.java.simpleName).formatted(Formatting.AQUA))
            .append(LiteralText("] ").formatted(Formatting.DARK_GRAY))
    }
}