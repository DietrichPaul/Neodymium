package de.cacheoverflow.jupiterclient.injections.mixins.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import de.cacheoverflow.jupiterclient.JupiterClient;
import de.cacheoverflow.jupiterclient.api.events.all.ChatEvent;
import de.cacheoverflow.jupiterclient.injections.interfaces.client.gui.screen.IScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Objects;

@Mixin(Screen.class)
public abstract class ScreenMixin extends DrawableHelper implements IScreenMixin {

    private static final Identifier DARK_BACKGROUND = new Identifier(JupiterClient.class.getSimpleName().toLowerCase(), "textures/backgrounds/dark.png");

    @Shadow @Nullable protected MinecraftClient client;

    @Shadow @Final private List<Drawable> drawables;

    @Shadow public int width;

    @Shadow public int height;

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

    /**
     * @author Mojang Studios, Cach30verfl0w
     * @reason Inject own background to all
     */
    @Overwrite
    public void renderBackground(MatrixStack matrices, int vOffset) {
        if (Objects.requireNonNull(this.client).world != null) {
            this.fillGradient(matrices, 0, 0, this.width, this.height, -0x3FEFEFF0, -0x2FEFEFF0);
        } else {
            RenderSystem.setShaderTexture(0, DARK_BACKGROUND);
            DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        }

    }

    @NotNull
    @Override
    public List<Drawable> getWidgets() {
        return this.drawables;
    }
}
