package de.cacheoverflow.neodymium.impl.utils

import com.google.common.base.Preconditions
import com.google.inject.Inject
import de.cacheoverflow.neodymium.api.utils.IChatHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class DefaultChatHelper @Inject constructor(client: MinecraftClient): IChatHelper {
    private val client: MinecraftClient

    init {
        Preconditions.checkNotNull(client, "The client cannot be null!")
        this.client = client
    }

    override fun sendComponent(text: Text, withPrefix: Boolean) {
        assert(client.player != null)
        client.player!!.sendMessage(
            (if (withPrefix) PREFIX.shallowCopy() else LiteralText("")).append(text).formatted(
                Formatting.GRAY
            ), false
        )
    }
}