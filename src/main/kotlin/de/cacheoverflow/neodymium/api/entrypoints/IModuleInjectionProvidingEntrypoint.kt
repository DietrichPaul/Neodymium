package de.cacheoverflow.neodymium.api.entrypoints

import com.google.inject.Module
import de.cacheoverflow.neodymium.Neodymium

interface IModuleInjectionProvidingEntrypoint {

    fun provide(client: Neodymium): List<Module>

}