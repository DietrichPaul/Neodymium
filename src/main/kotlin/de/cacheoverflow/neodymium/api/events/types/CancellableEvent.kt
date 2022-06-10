package de.cacheoverflow.neodymium.api.events.types

open class CancellableEvent: ICancellable {

    private var cancelled: Boolean = false

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.cancelled
    }

}