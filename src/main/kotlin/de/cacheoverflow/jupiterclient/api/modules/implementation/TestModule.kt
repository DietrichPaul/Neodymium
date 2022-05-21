package de.cacheoverflow.jupiterclient.api.modules.implementation

import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.events.EventTarget
import de.cacheoverflow.jupiterclient.api.events.all.update.PlayerUpdateEvent
import de.cacheoverflow.jupiterclient.api.modules.Module
import de.cacheoverflow.jupiterclient.api.utils.MathUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.Entity
import kotlin.math.hypot

@Module.Declaration("Test", "module.test.description", Module.EnumCategory.COMBAT, listenable = true)
class TestModule(client: JupiterClient) : Module(client) {

    @EventTarget
    fun handleUpdate(event: PlayerUpdateEvent) {
        var nearest: Double = 3.1
        var current: Entity? = null
        val minecraft: MinecraftClient = this.client.minecraft

        for (entity in minecraft.world!!.entities) {
            val distance: Double = hypot(entity.x - minecraft.player!!.x, entity.z - minecraft.player!!.z)
            if (distance <= nearest && entity !is ClientPlayerEntity) {
                current = entity
                nearest = distance
            }
        }

        if (current == null)
            return

        val lol: Array<Float> = MathUtils.getRotations(minecraft.player!!.eyePos!!, current.pos!!)

        minecraft.player!!.yaw = lol[0]
        minecraft.player!!.pitch = lol[1]
    }

}