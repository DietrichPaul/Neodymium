package de.cacheoverflow.jupiterclient.injections.mixins.client.network;

import de.cacheoverflow.jupiterclient.JupiterClient;
import de.cacheoverflow.jupiterclient.api.events.all.UpdateEvent;
import de.cacheoverflow.jupiterclient.api.events.all.update.PlayerUpdateEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    private static final PlayerUpdateEvent PRE_UPDATE_EVENT = new PlayerUpdateEvent(UpdateEvent.EnumType.PRE);
    private static final PlayerUpdateEvent POST_UPDATE_EVENT = new PlayerUpdateEvent(UpdateEvent.EnumType.POST);

    @Shadow @Final protected MinecraftClient client;

    @Inject(method = "tick", at = @At("HEAD"))
    public void injectTickOnPre(CallbackInfo callback) {
        JupiterClient.getInstanceFrom(this.client).getEventBus().callEvent(PRE_UPDATE_EVENT);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void injectTickOnPost(CallbackInfo callback) {
        JupiterClient.getInstanceFrom(this.client).getEventBus().callEvent(POST_UPDATE_EVENT);
    }

}
