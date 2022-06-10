package de.cacheoverflow.neodymium.api.events.all

import de.cacheoverflow.neodymium.api.events.types.CancellableEvent

class ChatEvent(
    val message: String,
    val addToHud: Boolean
): CancellableEvent()