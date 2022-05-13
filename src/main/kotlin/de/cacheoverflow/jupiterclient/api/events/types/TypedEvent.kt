package de.cacheoverflow.jupiterclient.api.events.types

open class TypedEvent<T: Enum<T>>(val type: T)