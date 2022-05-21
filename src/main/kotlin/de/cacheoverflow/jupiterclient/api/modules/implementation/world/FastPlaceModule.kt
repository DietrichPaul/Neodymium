package de.cacheoverflow.jupiterclient.api.modules.implementation.world

import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.events.EventTarget
import de.cacheoverflow.jupiterclient.api.events.all.update.PlayerUpdateEvent
import de.cacheoverflow.jupiterclient.api.modules.Module
import de.cacheoverflow.jupiterclient.injections.interfaces.client.IMinecraftClientMixin

@Module.Declaration("FastPlace", "module.fastplace.description", Module.EnumCategory.WORLD, listenable = true)
class FastPlaceModule(client: JupiterClient): Module(client) {

    @EventTarget
    fun handleUpdate(event: PlayerUpdateEvent) {
        (this.client.minecraft as IMinecraftClientMixin).setItemUseCooldown(0)
    }

}