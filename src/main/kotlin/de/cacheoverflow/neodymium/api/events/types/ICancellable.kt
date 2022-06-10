package de.cacheoverflow.neodymium.api.events.types

interface ICancellable {

    fun setCancelled(cancelled: Boolean)

    fun isCancelled(): Boolean

}