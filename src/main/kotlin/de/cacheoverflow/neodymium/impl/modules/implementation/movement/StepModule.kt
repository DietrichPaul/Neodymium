package de.cacheoverflow.neodymium.impl.modules.implementation.movement

import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.modules.Module

@Module.Declaration(name = "Step", description = "module.step.description", category = Module.EnumCategory.MOVEMENT)
class StepModule(client: Neodymium) : Module(client) {

    override fun onEnable() {
        client.minecraft.player!!.stepHeight = 1.0f
    }

    override fun onDisable() {
        client.minecraft.player!!.stepHeight = 0.6f
    }

}