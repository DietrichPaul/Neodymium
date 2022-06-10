package de.cacheoverflow.neodymium.injections.mixins.client.network;

import de.cacheoverflow.neodymium.Neodymium;
import de.cacheoverflow.neodymium.api.events.IEventBus;
import de.cacheoverflow.neodymium.api.events.all.UpdateEvent;
import de.cacheoverflow.neodymium.api.events.all.update.PlayerUpdateEvent;
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
        Neodymium.getInstanceFrom(this.client).get(IEventBus.class).callEvent(PRE_UPDATE_EVENT);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void injectTickOnPost(CallbackInfo callback) {
        Neodymium.getInstanceFrom(this.client).get(IEventBus.class).callEvent(POST_UPDATE_EVENT);
    }

}
