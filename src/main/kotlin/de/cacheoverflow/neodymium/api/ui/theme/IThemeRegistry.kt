package de.cacheoverflow.neodymium.api.ui.theme

import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.store.INameableRegistry

interface IThemeRegistry : INameableRegistry<ITheme> {

    fun setCurrentTheme(client: Neodymium, theme: String) {
        this.findFirstOr(theme, true, this.getCurrentTheme())?.let { this.setCurrentTheme(client, it) }
    }

    fun setCurrentTheme(client: Neodymium, theme: ITheme)

    fun getCurrentTheme(): ITheme

}