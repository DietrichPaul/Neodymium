package de.cacheoverflow.neodymium.injections.mixins.client;

import de.cacheoverflow.neodymium.Neodymium;
import de.cacheoverflow.neodymium.api.events.IEventBus;
import de.cacheoverflow.neodymium.api.events.all.KeyEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void injectOnKeyOnHead(long window, int key, int scancode, int action, int modifiers, CallbackInfo callback) {
        KeyEvent event = Neodymium.getInstanceFrom(client).get(IEventBus.class).callEvent(new KeyEvent(window, key, scancode, action, modifiers));
        if (event.isCancelled()) callback.cancel();
    }

}
