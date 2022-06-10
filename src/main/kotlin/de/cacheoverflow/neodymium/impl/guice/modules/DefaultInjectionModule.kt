package de.cacheoverflow.neodymium.impl.guice.modules

import com.google.inject.AbstractModule
import com.google.inject.multibindings.OptionalBinder
import de.cacheoverflow.neodymium.Neodymium
import de.cacheoverflow.neodymium.api.commands.ICommandRegistry
import de.cacheoverflow.neodymium.api.events.IEventBus
import de.cacheoverflow.neodymium.api.modules.IModuleRegistry
import de.cacheoverflow.neodymium.api.ui.theme.ITheme
import de.cacheoverflow.neodymium.api.ui.theme.IThemeRegistry
import de.cacheoverflow.neodymium.api.utils.IChatHelper
import de.cacheoverflow.neodymium.impl.commands.DefaultCommandRegistry
import de.cacheoverflow.neodymium.impl.events.DefaultEventBus
import de.cacheoverflow.neodymium.impl.modules.DefaultModuleRegistry
import de.cacheoverflow.neodymium.impl.ui.theme.DefaultThemeRegistry
import de.cacheoverflow.neodymium.impl.utils.DefaultChatHelper
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.client.MinecraftClient
import javax.inject.Provider

class DefaultInjectionModule(private val client: Neodymium): AbstractModule() {

    override fun configure() {
        this.bind(Neodymium::class.java).toInstance(this.client)
        this.bind(ModMetadata::class.java).toInstance(this.client.metadata)
        this.bind(MinecraftClient::class.java).toInstance(this.client.minecraft)

        this.bind(IChatHelper::class.java).to(DefaultChatHelper::class.java)

        OptionalBinder.newOptionalBinder(this.binder(), IEventBus::class.java).setBinding().toInstance(DefaultEventBus())
        OptionalBinder.newOptionalBinder(this.binder(), ICommandRegistry::class.java).setBinding().toInstance(DefaultCommandRegistry())
        OptionalBinder.newOptionalBinder(this.binder(), IModuleRegistry::class.java).setBinding().toInstance(DefaultModuleRegistry(this.client))
        OptionalBinder.newOptionalBinder(this.binder(), IThemeRegistry::class.java).setBinding().toInstance(DefaultThemeRegistry())
        this.bind(ITheme::class.java).toProvider(Provider {
            return@Provider client.injector.getInstance(IThemeRegistry::class.java).getCurrentTheme()
        })
    }

}