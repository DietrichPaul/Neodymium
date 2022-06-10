package de.cacheoverflow.neodymium.api.settings

import com.google.gson.JsonElement
import net.minecraft.util.StringIdentifiable
import java.util.function.Predicate

interface ISetting<T: Any>: StringIdentifiable {

    fun <E: JsonElement> toJson(): E

    fun setValue(value: T): Boolean

    fun fromJson(json: JsonElement)

    fun setValidator(predicate: Predicate<T?>?)

    fun getValidator(): Predicate<T?>?

    fun getDefaultValue(): T?

    fun getValue(): T?

}