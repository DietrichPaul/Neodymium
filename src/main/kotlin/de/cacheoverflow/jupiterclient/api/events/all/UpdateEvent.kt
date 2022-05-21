package de.cacheoverflow.jupiterclient.api.events.all

import de.cacheoverflow.jupiterclient.api.events.types.TypedEvent

open class UpdateEvent(type: EnumType) : TypedEvent<UpdateEvent.EnumType>(type) {

    enum class EnumType {
        PRE, POST
    }

}