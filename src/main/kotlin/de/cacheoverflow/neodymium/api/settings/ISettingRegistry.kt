package de.cacheoverflow.neodymium.api.settings

import de.cacheoverflow.neodymium.api.utils.ICopyable

interface ISettingRegistry: ICopyable<ISettingRegistry> {

    fun registerObject(identifier: String, obj: Any)

    fun findValuesBy(identifier: String): MutableCollection<ISetting<*>>

    fun findValueBy(identifier: String, name: String): ISetting<*>

    fun findAll(): MutableMap<String, MutableCollection<ISetting<*>>>

}