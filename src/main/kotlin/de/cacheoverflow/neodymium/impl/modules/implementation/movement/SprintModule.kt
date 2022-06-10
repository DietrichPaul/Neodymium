package de.cacheoverflow.neodymium.impl.modules.implementation.movement

import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.events.EventTarget
import de.cacheoverflow.neodymium.api.events.all.UpdateEvent
import de.cacheoverflow.neodymium.api.modules.Module
import net.minecraft.client.option.KeyBinding

@Module.Declaration("Sprint", "module.sprint.description", Module.EnumCategory.MOVEMENT, listenable = true)
class SprintModule(client: Neodymium): Module(client) {

    @EventTarget
    fun handleUpdate(event: UpdateEvent) {
        this.client.minecraft.options.sprintKey.isPressed = true
    }

    override fun onDisable() {
        KeyBinding.updatePressedStates()
    }

}