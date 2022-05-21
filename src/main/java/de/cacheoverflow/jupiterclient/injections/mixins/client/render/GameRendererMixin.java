package de.cacheoverflow.jupiterclient.injections.mixins.client.render;

import de.cacheoverflow.jupiterclient.JupiterClient;
import de.cacheoverflow.jupiterclient.api.modules.implementation.visual.BrightnessModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void injectGetNightVisionStrengthAtHead(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> callback) {
        BrightnessModule module = JupiterClient.getInstanceFrom(MinecraftClient.getInstance()).getModuleRegistry().findFirstOr("Brightness", false, null);
        assert module != null;

        if (module.getEnabled())
            callback.setReturnValue(module.getStrength());
    }

}
