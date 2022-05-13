package de.cacheoverflow.jupiterclient.api.events.all

import de.cacheoverflow.jupiterclient.api.events.types.CancellableEvent

class ChatEvent(
    val message: String,
    val addToHud: Boolean
): CancellableEvent()