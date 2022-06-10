package de.cacheoverflow.neodymium.impl.settings

import de.cacheoverflow.neodymium.api.settings.ISetting
import de.cacheoverflow.neodymium.api.settings.ISettingRegistry

class DefaultSettingRegistry(private val valueMap: HashMap<String, MutableCollection<ISetting<*>>> = HashMap()): ISettingRegistry {

    override fun registerObject(identifier: String, obj: Any) {
        val values: MutableList<ISetting<*>> = ArrayList()
        for (member in obj::class.java.fields) {
            member.isAccessible = true
            val value: Any = member.get(obj)
            if (value is ISetting<*>) {
                values.add(value as ISetting<*>)
            }
        }
        this.valueMap[identifier] = values
    }

    override fun findValuesBy(identifier: String): MutableCollection<ISetting<*>> {
        return ArrayList(this.valueMap[identifier]!!)
    }

    override fun findValueBy(identifier: String, name: String): ISetting<*> {
        return this.findValuesBy(identifier).filter { it.asString().equals(name, true) }[0]
    }

    override fun findAll(): MutableMap<String, MutableCollection<ISetting<*>>> {
        return HashMap(this.valueMap)
    }

    override fun copy(): ISettingRegistry {
        return DefaultSettingRegistry(this.valueMap)
    }

}