package de.cacheoverflow.jupiterclient.injections.mixins.client;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import de.cacheoverflow.jupiterclient.JupiterClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
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
public class MinecraftClientMixin {

    @Shadow @Final private static Logger LOGGER;
    @Unique private JupiterClient jupiterClient;

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

    @Inject(
            method = "stop",
            at = @At("HEAD")
    )
    public void injectStopOnHead(CallbackInfo callback) {
        this.jupiterClient.stop();
    }

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

}
