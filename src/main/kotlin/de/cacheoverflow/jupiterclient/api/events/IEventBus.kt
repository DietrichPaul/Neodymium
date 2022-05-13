package de.cacheoverflow.jupiterclient.api.events

import de.cacheoverflow.jupiterclient.api.store.IRegistry
import java.util.function.Predicate
import kotlin.reflect.KClass

interface IEventBus: IRegistry<IEventContainer> {

    fun registerListeners(listener: Array<Any>)

    fun <T: Any> callEvent(event: T, predicate: Predicate<IEventContainer>): T

    fun <T: Any> callEvent(event: T, listenerClass: KClass<Any>): T

    fun <T: Any> callEvent(event: T): T

}