package de.cacheoverflow.jupiterclient.api.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.pow

class Rotations(private val rotations: Array<Float> = arrayOf(0.0f, 0.0f)) {

    fun applySensitivity(client: MinecraftClient, previousRotations: Array<Float> = arrayOf(0.0f, 0.0f)): Rotations {
        val mouseSensitivity: Double = client.options.mouseSensitivity * 0.6000000238418579 + 0.20000000298023224
        val fixedSensitivity: Double = mouseSensitivity.pow(3) * 8.0

        return Rotations(MathUtils.changeLookDirection(
            previousRotations,
            (MathUtils.getAngleDifference(MathHelper.wrapDegrees(rotations[0]), MathHelper.wrapDegrees(rotations[1])) / 0.15) / fixedSensitivity,
            (MathUtils.getAngleDifference(MathHelper.wrapDegrees(rotations[0]), MathHelper.wrapDegrees(rotations[1])) / 0.15) / fixedSensitivity,
        ))
    }

    fun asFloatArray(): Array<Float> {
        return this.rotations
    }

    companion object {

        @JvmStatic fun calc(eyePosition: Vec3d, targetPosition: Vec3d): Rotations {
            val delta: Vec3d = targetPosition.subtract(eyePosition)
            return Rotations(arrayOf(
                MathHelper.wrapDegrees(Math.toDegrees(atan2(delta.z, delta.x) - Math.PI / 2).toFloat()),
                MathHelper.wrapDegrees(-Math.toDegrees(atan2(delta.y, hypot(delta.x, delta.z))).toFloat()))
            )
        }

    }

}