package de.cacheoverflow.jupiterclient.api.utils

import net.minecraft.util.math.MathHelper

object MathUtils {

    fun getAngleDifference(a: Float, b: Float): Float {
        return (a - b % 360f + 540f) % 360f - 180f
    }

    fun changeLookDirection(previous: Array<Float>, cursorDeltaX: Double, cursorDeltaY: Double): Array<Float> {
        return arrayOf(
            (previous[0] + cursorDeltaX * 0.15).toFloat(),
            MathHelper.clamp((previous[1] + cursorDeltaY * 0.15).toFloat(), -90F, 90F)
        )
    }

}