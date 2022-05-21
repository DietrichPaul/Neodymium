package de.cacheoverflow.jupiterclient.api.modules.implementation.movement

import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.modules.Module

@Module.Declaration(name = "Step", description = "module.step.description", category = Module.EnumCategory.MOVEMENT)
class StepModule(client: JupiterClient) : Module(client) {

    override fun onEnable() {
        client.minecraft.player!!.stepHeight = 1.0f
    }

    override fun onDisable() {
        client.minecraft.player!!.stepHeight = 0.6f
    }

}