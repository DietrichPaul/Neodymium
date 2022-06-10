package de.cacheoverflow.neodymium.impl.ui.theme.implementations.standard;

import com.mojang.blaze3d.systems.RenderSystem;
import de.cacheoverflow.neodymium.api.utils.ColorHelper;
import de.cacheoverflow.neodymium.api.utils.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class StandardButtonWidget extends ButtonWidget {

    private static final Color DESCRIPTION_COLOR = new Color(149, 149, 149);

    private static final double SCALE = 1 / 0.75;

    private final MinecraftClient client;
    private final Identifier imageId;
    private final Text description;
    private final int imageWidth;
    private final int imageHeight;
    private final int color;

    private boolean lastTimeHovered;
    private long hoverResetMilliseconds;

    public StandardButtonWidget(@NotNull final MinecraftClient client, @NotNull final Text text,
                                @NotNull final Text description, @NotNull final Identifier imageId, final int imageWidth,
                                final int imageHeight, final int x, final int y, final int width, final int height,
                                final int color, @NotNull final PressAction onPress) {
        super(x, y, width, height, text, onPress);
        this.client = client;
        this.imageId = imageId;
        this.description = description;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.color = color;
    }

    public StandardButtonWidget(@NotNull final MinecraftClient client, @NotNull final Text text, @NotNull final Identifier imageId,
                                final int imageWidth, final int imageHeight, final int x, final int y, final int width,
                                final int height, final int color, @NotNull final PressAction onPress) {
        this(client, text, LiteralText.EMPTY, imageId, imageWidth, imageHeight, x, y, width, height, color, onPress);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        matrices.push();
        matrices.scale(0.5f, 0.5f, 0.5f);
        if (this.active)
            RenderHelper.fillBorder(matrices, this.x * 2, this.y * 2, (this.x + this.width) * 2, (this.y +
                    this.height) * 2, ColorHelper.getArgbInt((int) Math.min(255, Math.max(0, this.isHovered() ?
                    System.currentTimeMillis() - hoverResetMilliseconds : 255 - Math.min(255, System.currentTimeMillis()
                    - this.hoverResetMilliseconds)))), 1);
        matrices.pop();

        DrawableHelper.fill(matrices, this.x, this.y, this.x + this.height, this.y + this.height, this.color);
        DrawableHelper.fill(matrices, this.x + this.height + 1, this.y, this.x + this.width, this.y + this.height, this.color);

        RenderSystem.setShaderTexture(0, this.imageId);
        DrawableHelper.drawTexture(matrices, this.x + this.height / 2 - this.imageWidth / 2, (this.y + this.height / 2) - this.imageHeight / 2, 0F, 0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        int fontHeight = this.client.textRenderer.fontHeight;
        this.client.textRenderer.drawWithShadow(matrices, this.getMessage(), this.x + this.height + 5,
                this.y + this.height / 2f - fontHeight / 2f - (this.description.equals(LiteralText.EMPTY) ? 0 : 4), this.active ? -1 : Color.GRAY.getRGB());

        if (!this.description.equals(LiteralText.EMPTY)) {
            matrices.push();
            matrices.scale(0.75f, 0.75f, 0.75f);
            this.client.textRenderer.drawWithShadow(matrices, this.description.asOrderedText(), (float) ((this.x + this.height + 5) * SCALE),
                    (float) ((this.y + this.height / 2 + fontHeight / 2f) * SCALE), active ? DESCRIPTION_COLOR.getRGB() : DESCRIPTION_COLOR.darker().getRGB());
            matrices.pop();
        }
        this.lastTimeHovered = this.isHovered();
    }
}
