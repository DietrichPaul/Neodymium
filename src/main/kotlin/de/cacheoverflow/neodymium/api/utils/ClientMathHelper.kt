package de.cacheoverflow.neodymium.api.utils

import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import kotlin.math.pow

object ClientMathHelper {

    @JvmStatic fun getAngleDifference(a: Float, b: Float): Float {
        return (a - b % 360f + 540f) % 360f - 180f
    }

    @JvmStatic fun changeLookDirection(previous: Array<Float>, cursorDeltaX: Double, cursorDeltaY: Double): Array<Float> {
        return arrayOf(
            (previous[0] + cursorDeltaX * 0.15).toFloat(),
            MathHelper.clamp((previous[1] + cursorDeltaY * 0.15).toFloat(), -90F, 90F)
        )
    }

    @JvmStatic fun isIn(box: Box, box1: Box): Boolean {
        return box.minX <= box1.minX && box.minY <= box1.minY && box.minZ <= box1.minZ && box.maxX >= box1.maxX && box.maxY >= box1.maxY && box.maxZ >= box1.maxY
    }

    @JvmStatic fun faculty(x: Long): Long {
        return if (x <= 1) 1 else x * faculty(x - 1)
    }

    @JvmStatic fun binomialCoefficient(n: Float, k: Float): Float {
        return if (k > n) 0f else if (k == 0f || k == 0f) 1f else binomialCoefficient(n - 1, k - 1) + binomialCoefficient(n - 1, k)
    }

    @JvmStatic fun bernoulliTrial(n: Float, p: Float, k: Float): Float {
        return this.binomialCoefficient(n, k) * p.pow(k) * (1 - p).pow(n - k)
    }

}