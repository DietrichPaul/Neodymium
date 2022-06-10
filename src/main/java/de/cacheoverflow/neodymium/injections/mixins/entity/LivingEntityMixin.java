package de.cacheoverflow.neodymium.injections.mixins.entity;

import de.cacheoverflow.neodymium.Neodymium;
import de.cacheoverflow.neodymium.api.modules.IModuleRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "hasStatusEffect", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("all")
    public void method(StatusEffect effect, CallbackInfoReturnable<Boolean> callback) {
        if (Neodymium.getInstanceFrom(MinecraftClient.getInstance()).get(IModuleRegistry.class)
                .findFirstOr("Brightness", false, null).getEnabled() && effect.equals(StatusEffects.NIGHT_VISION))
            callback.setReturnValue(true);
    }

}
