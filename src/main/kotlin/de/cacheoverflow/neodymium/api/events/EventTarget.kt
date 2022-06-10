package de.cacheoverflow.neodymium.api.events

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventTarget(val priority: Byte = 0)
