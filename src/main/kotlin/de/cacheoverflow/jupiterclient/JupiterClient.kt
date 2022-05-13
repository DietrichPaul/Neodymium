package de.cacheoverflow.jupiterclient

import com.google.common.base.Preconditions
import com.mojang.logging.LogUtils
import de.cacheoverflow.jupiterclient.injections.interfaces.client.IMinecraftClientMixin
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.client.MinecraftClient
import org.slf4j.Logger

class JupiterClient {

    val metadata: ModMetadata = FabricLoader.getInstance().getModContainer("jupiter-client").orElseThrow().metadata
    val logger: Logger = LogUtils.getLogger()

    companion object {
        fun getInstanceFrom(client: MinecraftClient): JupiterClient {
            Preconditions.checkNotNull(client, "The client can't be null!")
            return (client as IMinecraftClientMixin).jupiterClient
        }
    }

    fun start() {
        logger.info("Starting {} v{}...", metadata.name, metadata.version)
        logger.info("{} is now initialized.", metadata.name)
    }

    fun stop() {
        logger.info("Stopping {} v{}...", metadata.name, metadata.version)
        logger.info("{} is now stopped.", metadata.name)
    }

}