package de.cacheoverflow.jupiterclient

import com.google.common.base.Preconditions
import com.mojang.logging.LogUtils
import de.cacheoverflow.jupiterclient.api.events.EventTarget
import de.cacheoverflow.jupiterclient.api.events.IEventBus
import de.cacheoverflow.jupiterclient.api.events.all.ScreenEvent
import de.cacheoverflow.jupiterclient.impl.events.DefaultEventBus
import de.cacheoverflow.jupiterclient.injections.interfaces.client.IMinecraftClientMixin
import de.cacheoverflow.jupiterclient.ui.screens.MainMenuScreen
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.TitleScreen
import org.slf4j.Logger

class JupiterClient(
    private val minecraft: MinecraftClient
) {

    val metadata: ModMetadata = FabricLoader.getInstance().getModContainer("jupiter-client").orElseThrow().metadata
    val eventBus: IEventBus = DefaultEventBus()
    val logger: Logger = LogUtils.getLogger()

    companion object {
        @JvmStatic fun getInstanceFrom(client: MinecraftClient): JupiterClient {
            Preconditions.checkNotNull(client, "The client can't be null!")
            return (client as IMinecraftClientMixin).jupiterClient
        }
    }

    fun start() {
        logger.info("Starting {} v{}...", metadata.name, metadata.version)
        this.eventBus.registerListeners(arrayOf(this))
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
    }

}