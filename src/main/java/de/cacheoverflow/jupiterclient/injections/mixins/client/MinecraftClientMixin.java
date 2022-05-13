package de.cacheoverflow.jupiterclient.injections.mixins.client;

import de.cacheoverflow.jupiterclient.JupiterClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

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

}
