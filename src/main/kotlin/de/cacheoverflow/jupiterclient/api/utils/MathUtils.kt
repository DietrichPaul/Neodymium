package de.cacheoverflow.jupiterclient.api.utils

import net.minecraft.util.math.Vec3d
import kotlin.math.atan2
import kotlin.math.hypot

object MathUtils {

    fun getRotations(eyePosition: Vec3d, targetPosition: Vec3d): Array<Float> {
        val delta: Vec3d = targetPosition.subtract(eyePosition)
        return arrayOf(
            Math.toDegrees(atan2(delta.z, delta.x) - Math.PI / 2).toFloat(),
            -Math.toDegrees(atan2(delta.y, hypot(delta.x, delta.z))).toFloat()
        )
    }

}