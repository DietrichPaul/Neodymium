package de.cacheoverflow.jupiterclient.injections.mixins.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import de.cacheoverflow.jupiterclient.ui.screens.MainMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(value = EntryListWidget.class, priority = -1)
public abstract class EntryListWidgetMixin<E extends EntryListWidget.Entry<E>> extends AbstractParentElement implements Drawable, Selectable {

    @Shadow private boolean renderSelection;

    @Shadow protected abstract boolean isSelectedEntry(int index);

    @Shadow public abstract int getRowWidth();

    @Shadow protected abstract E getEntry(int index);

    @Shadow @Final protected int itemHeight;

    @Shadow protected int headerHeight;

    @Shadow protected int bottom;

    @Shadow protected int top;

    @Shadow protected abstract int getEntryCount();

    @Shadow protected abstract int getRowTop(int index);

    @Shadow protected abstract int getRowBottom(int index);

    @Shadow public abstract int getRowLeft();

    @Shadow protected abstract boolean isFocused();

    @Shadow protected int width;

    @Shadow protected int left;

    @Shadow @Final protected MinecraftClient client;

    @Shadow @Nullable private E hoveredEntry;

    /**
     * @author Cach30verfl0w, Cedric H.
     * @reason To fix the rendering of the EntryListWidget
     */
    @Overwrite
    public void renderList(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        int i = this.getEntryCount();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        for(int j = 0; j < i; ++j) {
            int k = this.getRowTop(j);
            int l = this.getRowBottom(j);
            if (l >= this.top && k <= this.bottom) {
                int m = y + j * this.itemHeight + this.headerHeight;
                int n = this.itemHeight - 4;
                E entry = this.getEntry(j);
                int o = this.getRowWidth();
                int p;
                if (this.renderSelection && this.isSelectedEntry(j)) {
                    p = (this.client.currentScreen instanceof MainMenuScreen ? this.left + MainMenuScreen.SCREEN_MOVE_FOR_MULTIPLAYER : this.left) + this.width / 2 - o / 2;
                    int q = (this.client.currentScreen instanceof MainMenuScreen ? this.left + MainMenuScreen.SCREEN_MOVE_FOR_MULTIPLAYER : this.left) + this.width / 2 + o / 2;
                    RenderSystem.disableTexture();
                    RenderSystem.setShader(GameRenderer::getPositionShader);
                    float f = this.isFocused() ? 1.0F : 0.5F;
                    RenderSystem.setShaderColor(f, f, f, 1.0F);
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
                    bufferBuilder.vertex((double)p, (double)(m + n + 2), 0.0D).next();
                    bufferBuilder.vertex((double)q, (double)(m + n + 2), 0.0D).next();
                    bufferBuilder.vertex((double)q, (double)(m - 2), 0.0D).next();
                    bufferBuilder.vertex((double)p, (double)(m - 2), 0.0D).next();
                    tessellator.draw();
                    RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
                    bufferBuilder.vertex((double)(p + 1), (double)(m + n + 1), 0.0D).next();
                    bufferBuilder.vertex((double)(q - 1), (double)(m + n + 1), 0.0D).next();
                    bufferBuilder.vertex((double)(q - 1), (double)(m - 1), 0.0D).next();
                    bufferBuilder.vertex((double)(p + 1), (double)(m - 1), 0.0D).next();
                    tessellator.draw();
                    RenderSystem.enableTexture();
                }

                p = this.getRowLeft();
                entry.render(matrices, j, k, p, o, n, mouseX, mouseY, Objects.equals(this.hoveredEntry, entry), delta);
            }
        }

    }

}
