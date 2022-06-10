package de.cacheoverflow.neodymium.api.settings

import java.util.function.Predicate

abstract class AbstractSetting<T: Any>(
    private val name: String,
    private val defaultValue: T? = null,
    private var validator: Predicate<T?>? = null,
    private var value: T? = defaultValue
): ISetting<T> {

    override fun setValue(value: T): Boolean {
        if (this.validator != null && !this.validator!!.test(value))
            return false

        this.value = value
        return true
    }

    override fun getValidator(): Predicate<T?>? {
        return this.validator
    }

    override fun getDefaultValue(): T? {
        return this.defaultValue
    }

    override fun getValue(): T? {
        return this.value
    }

    override fun asString(): String {
        return this.name
    }

    override fun setValidator(predicate: Predicate<T?>?) {
        this.validator = predicate
    }
}