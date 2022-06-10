package de.cacheoverflow.neodymium.impl.modules.implementation.visual

import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.modules.Module

@Module.Declaration("Brightness", "module.brightness.description", Module.EnumCategory.VISUAL)
class BrightnessModule(client: Neodymium): Module(client) {

    val strength: Float = 1.0f

}