package de.cacheoverflow.jupiterclient.api.events

class EventPriority {

    companion object {
        @JvmStatic val LOWEST: Byte = -2
        @JvmStatic val LOWER: Byte =  -1
        @JvmStatic val NORMAL: Byte =  0
        @JvmStatic val HIGHER: Byte =  1
        @JvmStatic val HIGHEST: Byte = 2
    }

    init {
        throw UnsupportedOperationException("This class is not intended to be initialized!")
    }

}