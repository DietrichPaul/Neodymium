package de.cacheoverflow.neodymium.impl.ui.theme

import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.store.IRegistry
import de.cacheoverflow.neodymium.api.ui.theme.ITheme
import de.cacheoverflow.neodymium.api.ui.theme.IThemeRegistry
import de.cacheoverflow.neodymium.impl.ui.theme.implementations.standard.StandardTheme
import java.util.function.Function
import java.util.function.Predicate

class DefaultThemeRegistry(
    private var IThemes: MutableCollection<ITheme> = ArrayList(),
    private var registerPredicate: Predicate<ITheme>? = Predicate { true },
    private var unregisterPredicate: Predicate<ITheme>? = Predicate { true },
    private var currentTheme: ITheme = StandardTheme()
): IThemeRegistry {
    
    override fun setCurrentTheme(client: Neodymium, theme: ITheme) {
        this.currentTheme = theme
    }

    override fun getCurrentTheme(): ITheme {
        return this.currentTheme
    }

    override fun <E> directAction(action: Function<MutableCollection<ITheme>, E?>): E? {
        return action.apply(this.IThemes)
    }

    override fun setRegisterPredicate(predicate: Predicate<ITheme>?) {
        this.registerPredicate = predicate
    }

    override fun setUnregisterPredicate(predicate: Predicate<ITheme>?) {
        this.unregisterPredicate = predicate
    }

    override fun getRegisterPredicate(): Predicate<ITheme>? {
        return this.registerPredicate
    }

    override fun getUnregisterPredicate(): Predicate<ITheme>? {
        return this.unregisterPredicate
    }

    override fun asList(): MutableCollection<ITheme> {
        return ArrayList(this.IThemes)
    }

    override fun iterator(): Iterator<ITheme> {
        return this.IThemes.iterator()
    }

    override fun copy(): IRegistry<ITheme> {
        return DefaultThemeRegistry(ArrayList(this.IThemes), registerPredicate, unregisterPredicate, this.currentTheme)
    }

}