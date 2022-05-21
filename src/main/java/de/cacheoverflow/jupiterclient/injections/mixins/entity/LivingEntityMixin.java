package de.cacheoverflow.jupiterclient.injections.mixins.entity;

import de.cacheoverflow.jupiterclient.JupiterClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = -1)
public abstract class LivingEntityMixin {

    @Inject(method = "hasStatusEffect", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("all")
    public void method(StatusEffect effect, CallbackInfoReturnable<Boolean> callback) {
        if (JupiterClient.getInstanceFrom(MinecraftClient.getInstance()).getModuleRegistry()
                .findFirstOr("Brightness", false, null).getEnabled() && effect.equals(StatusEffects.NIGHT_VISION))
            callback.setReturnValue(true);
    }

}
