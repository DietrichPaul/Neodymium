package de.cacheoverflow.jupiterclient.impl.events

import de.cacheoverflow.jupiterclient.api.events.EventTarget
import de.cacheoverflow.jupiterclient.api.events.IEventBus
import de.cacheoverflow.jupiterclient.api.events.IEventContainer
import de.cacheoverflow.jupiterclient.api.store.IRegistry
import java.lang.reflect.Method
import java.util.*
import java.util.function.Function
import java.util.function.Predicate
import kotlin.collections.ArrayList
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass

class DefaultEventBus(
    private var eventListener: MutableCollection<IEventContainer> = ArrayList(),
    private var registerPredicate: Predicate<IEventContainer>? = Predicate { true },
    private var unregisterPredicate: Predicate<IEventContainer>? = Predicate { true }
) : IEventBus {

    override fun registerListeners(listener: Array<Any>) {
        listener.forEach { entry ->
            run {

                Arrays.stream(entry.javaClass.methods)
                    .filter { method -> method.isAnnotationPresent(EventTarget::class.java) }
                    .filter { method -> method.parameterCount == 1 }
                    .forEach { method -> this.eventListener.add(object : IEventContainer {
                        override fun listener(): Any {
                            return entry
                        }

                        override fun method(): Method {
                            return method
                        }

                        override fun eventClass(): KClass<Any> {
                            return Reflection.createKotlinClass(method.parameterTypes[0])
                        }

                        override fun priority(): Byte {
                            return method.getAnnotation(EventTarget::class.java).priority
                        }

                        override fun toString(): String {
                            return this.listener().javaClass.name + "#" + this.method().name + "(" + this.eventClass().simpleName + ")" + ": " + this.priority()
                        }

                    }) }

            }
        }

        this.eventListener = ArrayList(this.eventListener.sortedWith(compareBy { it.priority() })) // I don't have a better idea ._.
    }

    override fun <T : Any> callEvent(event: T, predicate: Predicate<IEventContainer>): T {
        this.openStream { entry -> entry.eventClass().isInstance(event) && predicate.test(entry) }
            .forEach { entry -> entry.method().invoke(entry.listener(), event) }
        return event
    }

    override fun <T : Any> callEvent(event: T, listenerClass: KClass<Any>): T {
        return this.callEvent(event) { entry -> listenerClass.isInstance(entry.listener()) }
    }

    override fun <T : Any> callEvent(event: T): T {
        return this.callEvent(event) { true }
    }

    override fun <E> directAction(action: Function<MutableCollection<IEventContainer>, E?>): E? {
        return action.apply(this.eventListener)
    }

    override fun setRegisterPredicate(predicate: Predicate<IEventContainer>?) {
        this.registerPredicate = predicate
    }

    override fun setUnregisterPredicate(predicate: Predicate<IEventContainer>?) {
        this.unregisterPredicate = predicate
    }

    override fun getRegisterPredicate(): Predicate<IEventContainer>? {
        return this.registerPredicate
    }

    override fun getUnregisterPredicate(): Predicate<IEventContainer>? {
        return this.unregisterPredicate
    }

    override fun asList(): MutableCollection<IEventContainer> {
        return ArrayList(this.eventListener)
    }

    override fun iterator(): Iterator<IEventContainer> {
        return this.eventListener.iterator()
    }

    override fun copy(): IRegistry<IEventContainer> {
        return DefaultEventBus(ArrayList(this.eventListener))
    }

}