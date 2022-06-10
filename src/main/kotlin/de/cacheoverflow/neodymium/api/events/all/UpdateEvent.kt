package de.cacheoverflow.neodymium.api.events.all

import de.cacheoverflow.neodymium.api.events.types.TypedEvent

open class UpdateEvent(type: EnumType) : TypedEvent<UpdateEvent.EnumType>(type) {

    enum class EnumType {
        PRE, POST
    }

}