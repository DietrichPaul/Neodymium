package de.cacheoverflow.jupiterclient.api.modules

import de.cacheoverflow.jupiterclient.api.store.INameableRegistry
import de.cacheoverflow.jupiterclient.api.utils.IService

interface IModuleRegistry: INameableRegistry<Module>, IService {

}