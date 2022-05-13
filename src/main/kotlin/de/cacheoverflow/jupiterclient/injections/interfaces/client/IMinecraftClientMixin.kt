package de.cacheoverflow.jupiterclient.injections.interfaces.client

import de.cacheoverflow.jupiterclient.JupiterClient

interface IMinecraftClientMixin {

    /**
     * This method returns the instance of the client for the minecraft instance.
     *
     * @return The instance of the jupiter client, assigned to the minecraft client.
     *
     * @author Cach30verfl0w, Cedric H.
     * @since  1.0.0
     */
    val jupiterClient: JupiterClient

}