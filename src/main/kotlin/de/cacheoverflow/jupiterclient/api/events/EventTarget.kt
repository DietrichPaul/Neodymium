package de.cacheoverflow.jupiterclient.api.events

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventTarget(val priority: Byte = 0)
