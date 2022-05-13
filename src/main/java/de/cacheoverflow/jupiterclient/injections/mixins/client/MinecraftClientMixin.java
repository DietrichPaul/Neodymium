package de.cacheoverflow.jupiterclient.injections.mixins.client;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import de.cacheoverflow.jupiterclient.JupiterClient;
import de.cacheoverflow.jupiterclient.injections.interfaces.client.IMinecraftClientMixin;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
    @Unique private JupiterClient jupiterClient;

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
     * @see JupiterClient
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
        this.jupiterClient = new JupiterClient();
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
     * @see            JupiterClient
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
        this.jupiterClient.start();
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
     * @see            JupiterClient
     */
    @Inject(
            method = "stop",
            at = @At("HEAD")
    )
    public void injectStopOnHead(CallbackInfo callback) {
        this.jupiterClient.stop();
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
        StringBuilder titleBuilder = new StringBuilder(this.jupiterClient.getMetadata().getName());
        titleBuilder.append(" v").append(this.jupiterClient.getMetadata().getVersion());
        titleBuilder.append(" (").append(FabricLoader.getInstance().getAllMods().size()).append(" Mods loaded)");
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
     * This method makes the minecraft client able to return the jupiter client by the minecraft instance.
     *
     * @return The jupiter client instance in this class.
     *
     * @author Cach30verfl0w, Cedric H.
     */
    @Override
    public @NotNull JupiterClient getJupiterClient() {
        return this.jupiterClient;
    }

}
