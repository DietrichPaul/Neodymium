package de.cacheoverflow.jupiterclient.api.modules.implementation.visual

import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.modules.Module

@Module.Declaration("Brightness", "module.brightness.description", Module.EnumCategory.VISUAL)
class BrightnessModule(client: JupiterClient): Module(client) {

    val strength: Float = 1.0f

}