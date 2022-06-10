package de.cacheoverflow.neodymium

import com.google.common.base.Preconditions
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import com.mojang.logging.LogUtils
import de.cacheoverflow.neodymium.api.commands.ICommandRegistry
import de.cacheoverflow.neodymium.api.entrypoints.IModuleInjectionProvidingEntrypoint
import de.cacheoverflow.neodymium.api.events.EventTarget
import de.cacheoverflow.neodymium.api.events.IEventBus
import de.cacheoverflow.neodymium.api.events.all.ChatEvent
import de.cacheoverflow.neodymium.api.events.all.KeyEvent
import de.cacheoverflow.neodymium.api.events.all.ScreenEvent
import de.cacheoverflow.neodymium.api.modules.IModuleRegistry
import de.cacheoverflow.neodymium.impl.utils.DefaultChatHelper
import de.cacheoverflow.neodymium.impl.commands.implementation.TestCommand
import de.cacheoverflow.neodymium.impl.commands.implementation.ToggleCommand
import de.cacheoverflow.neodymium.impl.guice.modules.DefaultInjectionModule
import de.cacheoverflow.neodymium.impl.screens.ClickGuiScreen
import de.cacheoverflow.neodymium.impl.screens.MainMenuScreen
import de.cacheoverflow.neodymium.injections.interfaces.client.IMinecraftClientMixin
import de.cacheoverflow.neodymium.injections.interfaces.client.gui.screen.multiplayer.IMultiplayerScreenMixin
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.util.Session
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import java.util.*
import kotlin.reflect.KClass

class Neodymium(
    val minecraft: MinecraftClient
) {

    val metadata: ModMetadata = FabricLoader.getInstance().getModContainer(Neodymium::class.java.simpleName.lowercase()).orElseThrow().metadata
    val logger: Logger = LogUtils.getLogger()
    val chatHelper: DefaultChatHelper = DefaultChatHelper(this.minecraft)

    lateinit var injector: Injector

    companion object {
        @JvmStatic fun getInstanceFrom(client: MinecraftClient): Neodymium {
            Preconditions.checkNotNull(client, "The client can't be null!")
            return (client as IMinecraftClientMixin).neodymium
        }
    }

    fun start() {
        logger.info("Starting {} v{}...", metadata.name, metadata.version)
        val modules: MutableList<Module> = arrayListOf(DefaultInjectionModule(this))
        FabricLoader.getInstance().getEntrypointContainers(Neodymium::class.java.simpleName, IModuleInjectionProvidingEntrypoint::class.java).forEach {
            modules.addAll(it.entrypoint.provide(this))
        }
        this.injector = Guice.createInjector(modules)

        this.get(IEventBus::class).registerListeners(arrayOf(this))
        this.get(ICommandRegistry::class).register(arrayOf(TestCommand(), ToggleCommand(this.get(IModuleRegistry::class))))

        this.get(IModuleRegistry::class).start()
        logger.info("{} is now initialized.", metadata.name)
        (this.minecraft as IMinecraftClientMixin).setSession(Session("NeodymiumUser" + Random().nextInt(999), "", "", Optional.empty(), Optional.empty(), Session.AccountType.LEGACY))
    }

    fun stop() {
        logger.info("Stopping {} v{}...", metadata.name, metadata.version)
        logger.info("{} is now stopped.", metadata.name)
    }

    @EventTarget
    @Suppress("unused")
    fun listenToScreenEvent(event: ScreenEvent) {
        if (event.screen is TitleScreen || (event.screen == null && this.minecraft.world == null))
            event.screen = MainMenuScreen()
        else if (event.screen is MultiplayerScreen)
            event.screen = MainMenuScreen(((event.screen as IMultiplayerScreenMixin).getParentScreen()))
    }

    @EventTarget
    @Suppress("unused")
    fun listenToChatEvent(event: ChatEvent) {
        if (!this.get(ICommandRegistry::class).processCommand(this, event.message))
            event.setCancelled(true)
    }

    @EventTarget
    @Suppress("unused")
    fun listenToKey(event: KeyEvent) {
        if (event.key == GLFW.GLFW_KEY_RIGHT_SHIFT)
            this.minecraft.setScreen(ClickGuiScreen()) // Move to macros
    }

    fun <T: Any> get(type: KClass<T>): T {
        return this.get(type.java)
    }

    fun <T: Any> get(type: Class<T>): T {
        return this.injector.getInstance(type)
    }

}