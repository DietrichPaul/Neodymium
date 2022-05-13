package de.cacheoverflow.jupiterclient.api.events.types

class TypedEvent<T: Enum<T>>(val type: T)