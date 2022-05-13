package de.cacheoverflow.jupiterclient.injections.interfaces.client;

import de.cacheoverflow.jupiterclient.JupiterClient;
import org.jetbrains.annotations.NotNull;

public interface IMinecraftClientMixin {

    /**
     * This method returns the instance of the client for the minecraft instance.
     *
     * @return The instance of the jupiter client, assigned to the minecraft client.
     *
     * @author Cach30verfl0w, Cedric H.
     * @since  1.0.0
     */
    @NotNull JupiterClient getJupiterClient();

}
