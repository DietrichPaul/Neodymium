package de.cacheoverflow.jupiterclient;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import de.cacheoverflow.jupiterclient.injections.interfaces.client.IMinecraftClientMixin;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class JupiterClient {

    private final Logger logger = LogUtils.getLogger();
    private final ModMetadata metadata;

    public JupiterClient() {
        this.metadata = FabricLoader.getInstance().getModContainer("jupiter-client").orElseThrow().getMetadata();
    }

    public void start() {
        this.logger.info("Starting {} v{}...", this.metadata.getName(), this.metadata.getVersion());
        this.logger.info("{} is now initialized.", this.metadata.getName());
    }

    public void stop() {
        this.logger.info("Stopping {} v{}...", this.metadata.getName(), this.metadata.getVersion());
        this.logger.info("{} is now stopped.", this.metadata.getName());
    }

    public static @NotNull JupiterClient getInstanceFrom(@NotNull final MinecraftClient client) {
        Preconditions.checkNotNull(client, "The client can't be null!");
        return ((IMinecraftClientMixin) client).getJupiterClient();
    }

    public @NotNull ModMetadata getMetadata() {
        return metadata;
    }

    public @NotNull Logger getLogger() {
        return logger;
    }

}
