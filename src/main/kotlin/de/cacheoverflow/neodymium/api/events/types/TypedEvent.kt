package de.cacheoverflow.neodymium.api.events.types

open class TypedEvent<T: Enum<T>>(val type: T)