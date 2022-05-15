package de.cacheoverflow.jupiterclient.api.utils

import java.awt.Color

class ColorHelper private constructor() {

    init {
        throw IllegalStateException("This class is not intended to be instantiated!")
    }

    companion object {
        @JvmStatic fun getArgb(argb: Int): Color {
            return Color(argb, argb, argb, argb)
        }

        @JvmStatic
        fun getArgbInt(argb: Int): Int {
            return getArgb(argb).rgb
        }

        @JvmStatic fun changeAlpha(color: Color, alpha: Float): Color {
            return Color(color.red / 255f, color.green / 255f, color.blue / 255f, alpha)
        }

        @JvmStatic fun changeAlpha(color: Int, alpha: Float): Int {
            return changeAlpha(Color(color), alpha).rgb
        }
    }
}