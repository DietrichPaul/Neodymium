package de.cacheoverflow.jupiterclient.injections.mixins.client.gui.screen;

import de.cacheoverflow.jupiterclient.JupiterClient;
import de.cacheoverflow.jupiterclient.api.events.all.ChatEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Screen.class)
public abstract class ScreenMixin extends DrawableHelper {

    @Shadow @Nullable protected MinecraftClient client;

    /**
     * Rewrite the {@link Screen#sendMessage(String)} method in the {@link Screen} class to hook the event into the
     * chat message send-operation.
     *
     * @reason Inject the chat event for message processing
     * @author Mojang Studios, Cach30verfl0w
     *
     * @see    de.cacheoverflow.jupiterclient.api.events.IEventBus
     * @see    ChatEvent
     */
    @Overwrite
    public void sendMessage(@NotNull final String message, final boolean toHud) {
        assert this.client != null;
        ChatEvent event = JupiterClient.getInstanceFrom(this.client).getEventBus().callEvent(new ChatEvent(message, toHud));
        if (event.isCancelled())
            return;

        if (event.getAddToHud())
            this.client.inGameHud.getChatHud().addToMessageHistory(event.getMessage());

        assert this.client.player != null;
        this.client.player.sendChatMessage(event.getMessage());
    }

}
