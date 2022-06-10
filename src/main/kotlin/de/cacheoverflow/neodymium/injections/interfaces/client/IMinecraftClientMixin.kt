package de.cacheoverflow.neodymium.injections.interfaces.client

import de.cacheoverflow.neodymium.Neodymium
import net.minecraft.client.util.Session

interface IMinecraftClientMixin {

    /**
     * This method returns the instance of the client for the minecraft instance.
     *
     * @return The instance of the jupiter client, assigned to the minecraft client.
     *
     * @author Cach30verfl0w, Cedric H.
     * @since  1.0.0
     */
    val neodymium: Neodymium

    fun setSession(session: Session)

    fun setItemUseCooldown(ticks: Int)

    fun getItemUseCooldown(): Int

}