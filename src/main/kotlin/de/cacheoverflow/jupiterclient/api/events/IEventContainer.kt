package de.cacheoverflow.jupiterclient.api.events

import java.lang.reflect.Method
import kotlin.reflect.KClass

interface IEventContainer {

    fun listener(): Any

    fun method(): Method

    fun eventClass(): KClass<Any>

    fun priority(): Byte

}