package de.cacheoverflow.jupiterclient

import com.google.common.base.Preconditions
import com.mojang.logging.LogUtils
import de.cacheoverflow.jupiterclient.api.commands.ICommandRegistry
import de.cacheoverflow.jupiterclient.api.commands.implementation.TestCommand
import de.cacheoverflow.jupiterclient.api.events.EventTarget
import de.cacheoverflow.jupiterclient.api.events.IEventBus
import de.cacheoverflow.jupiterclient.api.events.all.ChatEvent
import de.cacheoverflow.jupiterclient.api.events.all.ScreenEvent
import de.cacheoverflow.jupiterclient.api.utils.ChatHelper
import de.cacheoverflow.jupiterclient.impl.commands.DefaultCommandRegistry
import de.cacheoverflow.jupiterclient.impl.events.DefaultEventBus
import de.cacheoverflow.jupiterclient.injections.interfaces.client.IMinecraftClientMixin
import de.cacheoverflow.jupiterclient.injections.interfaces.client.gui.screen.multiplayer.IMultiplayerScreenMixin
import de.cacheoverflow.jupiterclient.ui.screens.MainMenuScreen
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import org.slf4j.Logger

class JupiterClient(
    private val minecraft: MinecraftClient
) {

    val metadata: ModMetadata = FabricLoader.getInstance().getModContainer("jupiter-client").orElseThrow().metadata
    val commandRegistry: ICommandRegistry<JupiterClient> = DefaultCommandRegistry()
    val eventBus: IEventBus = DefaultEventBus()
    val logger: Logger = LogUtils.getLogger()
    val chatHelper: ChatHelper = ChatHelper(this.minecraft)

    companion object {
        @JvmStatic fun getInstanceFrom(client: MinecraftClient): JupiterClient {
            Preconditions.checkNotNull(client, "The client can't be null!")
            return (client as IMinecraftClientMixin).jupiterClient
        }
    }

    fun start() {
        logger.info("Starting {} v{}...", metadata.name, metadata.version)
        this.eventBus.registerListeners(arrayOf(this))
        this.commandRegistry.register(arrayOf(TestCommand()))
        logger.info("{} is now initialized.", metadata.name)
    }

    fun stop() {
        logger.info("Stopping {} v{}...", metadata.name, metadata.version)
        logger.info("{} is now stopped.", metadata.name)
    }

    @EventTarget
    @Suppress("unused")
    fun listenToScreenEvent(event: ScreenEvent) {
        if (event.screen is TitleScreen || (event.screen == null && this.minecraft.world == null))
            event.screen = MainMenuScreen()
        else if (event.screen is MultiplayerScreen)
            event.screen = MainMenuScreen(((event.screen as IMultiplayerScreenMixin).getParentScreen()))
    }

    @EventTarget
    @Suppress("unused")
    fun listenToChatEvent(event: ChatEvent) {
        if (!this.commandRegistry.processCommand(this, event.message))
            event.setCancelled(true)

    }

}