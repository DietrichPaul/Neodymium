package de.cacheoverflow.neodymium.injections.mixins.client;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import de.cacheoverflow.neodymium.Neodymium;
import de.cacheoverflow.neodymium.api.events.IEventBus;
import de.cacheoverflow.neodymium.api.events.all.ScreenEvent;
import de.cacheoverflow.neodymium.injections.interfaces.client.IMinecraftClientMixin;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Session;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements IMinecraftClientMixin {

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Nullable public abstract ClientPlayNetworkHandler getNetworkHandler();

    @Shadow @Nullable private IntegratedServer server;

    @Shadow public abstract boolean isConnectedToRealms();

    @Shadow @Nullable private ServerInfo currentServerEntry;
    @Shadow @Nullable public Screen currentScreen;
    @Shadow private int itemUseCooldown;
    @Shadow @Final @Mutable private Session session;

    @Unique private Neodymium neodymium;

    /**
     * Inject into the constructor at {@link net.minecraft.util.thread.ReentrantThreadExecutor#ReentrantThreadExecutor(String)}
     * in the {@link MinecraftClient} class, to initialize the client.
     *
     * @author         Cach30verfl0w
     * @reason         Make the client able to initialize
     *
     * @param args     The arguments of minecraft
     * @param callback The callback of the mixin
     *
     * @see Neodymium
     */
    @Inject(
            method = "<init>",
            at = @At(
                    target = "Lnet/minecraft/util/thread/ReentrantThreadExecutor;<init>(Ljava/lang/String;)V",
                    shift = At.Shift.AFTER,
                    value = "INVOKE"
            )
    )
    public void injectConstructorAfterSuper(RunArgs args, CallbackInfo callback) {
        this.neodymium = new Neodymium(MinecraftClient.class.cast(this));
    }

    /**
     * Inject into the constructor at {@link net.minecraft.client.util.Window#setFramerateLimit} in the
     * {@link MinecraftClient} class, to make the client able to start.
     *
     * @author         Cach30verfl0w
     * @reason         Make the client able to start
     *
     * @param args     The arguments of minecraft
     * @param callback The callback of the mixin
     *
     * @see            Neodymium
     */
    @Inject(
            method = "<init>",
            at = @At(
                    target = "Lnet/minecraft/client/util/Window;setFramerateLimit(I)V",
                    shift = At.Shift.BEFORE,
                    value = "INVOKE"
            )
    )
    public void injectConstructorBeforeFrameLimit(RunArgs args, CallbackInfo callback) {
        this.neodymium.start();
    }

    /**
     * Inject into the {@link MinecraftClient#stop} method at the head of the method in the {@link MinecraftClient}
     * class, to make the client able to stop.
     *
     * @author         Cach30verfl0w
     * @reason         Make the client able to stop
     *
     * @param callback The callback of the mixin
     *
     * @see            Neodymium
     */
    @Inject(
            method = "stop",
            at = @At("HEAD")
    )
    public void injectStopOnHead(CallbackInfo callback) {
        this.neodymium.stop();
    }

    /**
     * Hook into the {@link MinecraftClient#setScreen} method to manipulate the screen argument with the ScreenEvent.
     *
     * @param screen The screen parameter.
     * @return       The new screen parameter.
     *
     * @see          ScreenEvent
     * @see          de.cacheoverflow.neodymium.api.events.IEventBus
     */
    @ModifyVariable(method = "setScreen", at = @At(value = "HEAD"), argsOnly = true)
    public Screen hookSetScreen(Screen screen) {
        return this.neodymium.get(IEventBus.class).callEvent(new ScreenEvent(this.currentScreen, screen)).getScreen();
    }

    @Override
    public void setSession(@NotNull Session session) {
        this.session = session;
    }

    /**
     * Inject into the {@link MinecraftClient#getWindowTitle} method at the head of the method in the
     * {@link MinecraftClient} class, to display the client name on the title of the window.
     *
     * @author         Cach30verfl0w
     * @reason         Display the client name on the title of the window.
     *
     * @param callback The callback of the mixin
     *
     * @see            StringBuilder
     */
    @Inject(
            method = "getWindowTitle",
            at = @At("HEAD"),
            cancellable = true
    )
    private void injectGetWindowTitleOnHead(CallbackInfoReturnable<String> callback) {
        StringBuilder titleBuilder = new StringBuilder();
        try {
            titleBuilder.append(this.neodymium.getMetadata().getName()).append(" v").append(this.neodymium.getMetadata().getVersion());
            titleBuilder.append(" (").append(FabricLoader.getInstance().getAllMods().size()).append(" Mods loaded)");
        } catch (Exception ignored) {

        }
        titleBuilder.append(" - Minecraft ").append(SharedConstants.getGameVersion().getName());

        ClientPlayNetworkHandler networkHandler = this.getNetworkHandler();
        if (networkHandler != null && networkHandler.getConnection().isOpen()) {
            titleBuilder.append(" | ");

            if (this.server != null && !this.server.isRemote())
                titleBuilder.append(I18n.translate("title.singleplayer"));
            else if (this.isConnectedToRealms())
                titleBuilder.append(I18n.translate("title.multiplayer.realms"));
            else if (this.server == null && (this.currentServerEntry == null || !this.currentServerEntry.isLocal()))
                titleBuilder.append(I18n.translate("title.multiplayer.other"));
            else
                titleBuilder.append(I18n.translate("title.multiplayer.lan"));
        }
        callback.setReturnValue(titleBuilder.toString());
    }

    /**
     * Inject into the {@link MinecraftClient#createUserApiService} method at {@link Logger#error(String, Throwable)} in
     * the {@link MinecraftClient} class, to remove the exception and print a message.
     *
     * @author         Cach30verfl0w
     * @reason         Remove the exception, if you start minecraft in the IDE etc.
     *
     * @param callback The callback of the mixin
     *
     * @see Logger#warn(String)
     */
    @Inject(
            method = "createUserApiService",
            at = @At(
                    target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V",
                    shift = At.Shift.BEFORE,
                    value = "INVOKE"
            ),
            cancellable = true
    )
    public void injectCreateUserApiServiceBeforeException(YggdrasilAuthenticationService authService, RunArgs runArgs, CallbackInfoReturnable<UserApiService> callback) {
        LOGGER.warn("Can't authenticate account. Switch to offline authentication...");
        callback.setReturnValue(UserApiService.OFFLINE);
    }

    /**
     * This method makes the minecraft client able to return the neodymium mod by the minecraft instance.
     *
     * @return The neodymium instance in this class.
     *
     * @author Cach30verfl0w, Cedric H.
     */
    @Override
    public @NotNull Neodymium getNeodymium() {
        return this.neodymium;
    }

    @Override
    public void setItemUseCooldown(int itemUseCooldown) {
        this.itemUseCooldown = itemUseCooldown;
    }

    @Override
    public int getItemUseCooldown() {
        return this.itemUseCooldown;
    }
}
