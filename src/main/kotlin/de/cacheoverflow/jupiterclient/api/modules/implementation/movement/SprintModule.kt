package de.cacheoverflow.jupiterclient.api.modules.implementation.movement

import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.events.EventTarget
import de.cacheoverflow.jupiterclient.api.events.all.UpdateEvent
import de.cacheoverflow.jupiterclient.api.modules.Module
import net.minecraft.client.option.KeyBinding

@Module.Declaration("Sprint", "module.sprint.description", Module.EnumCategory.MOVEMENT, listenable = true)
class SprintModule(client: JupiterClient): Module(client) {

    @EventTarget
    fun handleUpdate(event: UpdateEvent) {
        this.client.minecraft.options.sprintKey.isPressed = true
    }

    override fun onDisable() {
        KeyBinding.updatePressedStates()
    }

}