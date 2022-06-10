package de.cacheoverflow.neodymium.impl.modules

import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.modules.IModuleRegistry
import de.cacheoverflow.neodymium.api.modules.Module
import de.cacheoverflow.neodymium.impl.modules.implementation.movement.SprintModule
import de.cacheoverflow.neodymium.impl.modules.implementation.TestModule
import de.cacheoverflow.neodymium.impl.modules.implementation.movement.StepModule
import de.cacheoverflow.neodymium.impl.modules.implementation.visual.BrightnessModule
import de.cacheoverflow.neodymium.impl.modules.implementation.world.FastPlaceModule
import de.cacheoverflow.neodymium.api.store.IRegistry
import java.util.function.Function
import java.util.function.Predicate

class DefaultModuleRegistry(
    private val client: Neodymium,
    private var modules: MutableCollection<Module> = ArrayList(),
    private var registerPredicate: Predicate<Module>? = Predicate { true },
    private var unregisterPredicate: Predicate<Module>? = Predicate { true }
): IModuleRegistry {

    override fun start() {
        this.register(arrayOf(TestModule(client), SprintModule(client), BrightnessModule(client), StepModule(client), FastPlaceModule(client)))
    }

    override fun <E> directAction(action: Function<MutableCollection<Module>, E?>): E? {
        return action.apply(this.modules)
    }

    override fun setRegisterPredicate(predicate: Predicate<Module>?) {
        this.registerPredicate = predicate
    }

    override fun setUnregisterPredicate(predicate: Predicate<Module>?) {
        this.unregisterPredicate = predicate
    }

    override fun getRegisterPredicate(): Predicate<Module>? {
        return this.registerPredicate
    }

    override fun getUnregisterPredicate(): Predicate<Module>? {
        return this.unregisterPredicate
    }

    override fun asList(): MutableCollection<Module> {
        return ArrayList(this.modules)
    }

    override fun iterator(): Iterator<Module> {
        return this.modules.iterator()
    }

    override fun copy(): IRegistry<Module> {
        return DefaultModuleRegistry(this.client, ArrayList(this.modules), registerPredicate, unregisterPredicate)
    }

}