package de.cacheoverflow.jupiterclient.injections.mixins.client.gui.screen.multiplayer;

import de.cacheoverflow.jupiterclient.ui.screens.MainMenuScreen;
import de.cacheoverflow.jupiterclient.injections.interfaces.client.gui.screen.multiplayer.IMultiplayerScreenMixin;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.LanServerQueryManager;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen implements IMultiplayerScreenMixin {

    @Shadow protected MultiplayerServerListWidget serverListWidget;

    @Shadow protected abstract void directConnect(boolean confirmedAction);

    @Shadow protected abstract void addEntry(boolean confirmedAction);

    @Shadow protected abstract void editEntry(boolean confirmedAction);

    @Shadow protected abstract void connect(ServerInfo entry);

    @Shadow private ServerInfo selectedEntry;

    @Shadow protected abstract void refresh();

    @Shadow private LanServerQueryManager.LanServerEntryList lanServers;

    @Shadow @Final private MultiplayerServerListPinger serverListPinger;

    @Shadow private ButtonWidget buttonDelete;

    @Shadow protected abstract void removeEntry(boolean confirmedAction);

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void injectInitReturn(CallbackInfo ci) {
        if (Objects.requireNonNull(this.client).currentScreen instanceof MultiplayerScreen)
            return;

        this.serverListWidget.setRenderBackground(false);
        this.serverListWidget.setRenderHorizontalShadows(false);
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerScreen;serverListWidget:Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerServerListWidget;",
                    shift = At.Shift.AFTER
            )
    )
    public void initField(CallbackInfo callback) {
        this.serverListWidget.updateSize(this.width - MainMenuScreen.SCREEN_MOVE_FOR_MULTIPLAYER, this.height, 32, this.height - 64);
        this.serverListWidget.setLeftPos(MainMenuScreen.SCREEN_MOVE_FOR_MULTIPLAYER);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerServerListWidget;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
                    shift = At.Shift.AFTER
            )
    )
    public void injectRenderPost(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo callback) {
        if (Objects.requireNonNull(this.client).currentScreen instanceof MultiplayerScreen)
            return;

        matrices.translate(MainMenuScreen.SCREEN_MOVE_FOR_MULTIPLAYER, 0, 0);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        if (!(Objects.requireNonNull(this.client).currentScreen instanceof MultiplayerScreen))
            return;

        super.renderBackground(matrices);

    }

    @Override
    public void doDirectConnect(boolean confirmedAction) {
        this.directConnect(confirmedAction);
    }

    @Override
    public void doAddEntry(boolean confirmedAction) {
        this.addEntry(confirmedAction);
    }

    @Override
    public void doEditEntry(boolean confirmedAction) {
        this.editEntry(confirmedAction);
    }

    @Override
    public void doDeleteEntry(boolean confirmedAction) {
        this.removeEntry(confirmedAction);
    }

    @Override
    public void doConnect(@NotNull ServerInfo serverInfo) {
        this.connect(serverInfo);
    }

    @Override
    public void setSelectedEntry(ServerInfo serverInfo) {
        this.selectedEntry = serverInfo;
    }

    @Override
    public ButtonWidget getDeleteButton() {
        return this.buttonDelete;
    }

    @Override
    public void refreshScreen() {
        this.refresh();
    }

    @Override
    public LanServerQueryManager.LanServerEntryList getLanServerEntryList() {
        return this.lanServers;
    }

    @Override
    public MultiplayerServerListPinger getServerListPinger() {
        return this.serverListPinger;
    }

    @Override
    public MultiplayerServerListWidget getEntryList() {
        return this.serverListWidget;
    }

    @Override
    public ServerInfo getSelectedEntry() {
        return this.selectedEntry;
    }
}
