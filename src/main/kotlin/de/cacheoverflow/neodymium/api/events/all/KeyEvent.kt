package de.cacheoverflow.neodymium.api.events.all

import de.cacheoverflow.neodymium.api.events.types.CancellableEvent

class KeyEvent(val window: Long, val key: Int, val scancode: Int, val action: Int, val modifiers: Int): CancellableEvent()