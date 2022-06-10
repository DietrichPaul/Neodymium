package de.cacheoverflow.neodymium.impl.modules.implementation.world

import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.events.EventTarget
import de.cacheoverflow.neodymium.api.events.all.update.PlayerUpdateEvent
import de.cacheoverflow.neodymium.api.modules.Module
import de.cacheoverflow.neodymium.injections.interfaces.client.IMinecraftClientMixin

@Module.Declaration("FastPlace", "module.fastplace.description", Module.EnumCategory.WORLD, listenable = true)
class FastPlaceModule(client: Neodymium): Module(client) {

    @EventTarget
    fun handleUpdate(event: PlayerUpdateEvent) {
        (this.client.minecraft as IMinecraftClientMixin).setItemUseCooldown(0)
    }

}