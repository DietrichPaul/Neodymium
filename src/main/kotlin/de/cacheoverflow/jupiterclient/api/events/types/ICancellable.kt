package de.cacheoverflow.jupiterclient.api.events.types

interface ICancellable {

    fun setCancelled(cancelled: Boolean)

    fun isCancelled(): Boolean

}