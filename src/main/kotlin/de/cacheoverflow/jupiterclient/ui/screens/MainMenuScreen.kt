package de.cacheoverflow.jupiterclient.ui.screens

import com.mojang.blaze3d.systems.RenderSystem
import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.ui.ScreenFacade
import de.cacheoverflow.jupiterclient.api.utils.ButtonHelper
import de.cacheoverflow.jupiterclient.api.utils.ColorHelper
import de.cacheoverflow.jupiterclient.injections.interfaces.client.gui.screen.multiplayer.IMultiplayerScreenMixin
import net.minecraft.client.gui.screen.AddServerScreen
import net.minecraft.client.gui.screen.ConfirmScreen
import net.minecraft.client.gui.screen.DirectConnectScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ScreenTexts
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget.ServerEntry
import net.minecraft.client.gui.screen.option.OptionsScreen
import net.minecraft.client.gui.screen.world.SelectWorldScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.network.ServerInfo
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import java.awt.Color
import java.util.*

class MainMenuScreen(
    private val prevScreen: Screen? = null
) : ScreenFacade("narrator.screen.title") {

    private val multiplayerScreen: MultiplayerScreen = MultiplayerScreen(this)
    private var deleteButton: ButtonWidget? = null
    private var editButton: ButtonWidget? = null

    override fun init() {
        if (this.prevScreen != null)
            this.prevScreen.let { this.setEscapeScreen(it) }
        multiplayerScreen.init(client, width, height + 64)
        ButtonHelper.clearButtons(multiplayerScreen)

        /* Buttons at the top of the bar in the screen */addButtonWidget(
            TranslatableText("menu.singleplayer"), TranslatableText("button.singleplayer.description"),
            PLAY_SINGLEPLAYER, 5, 32, 140,
            25, ColorHelper.changeAlpha(Color.GREEN.rgb, 0.5f), 16, 16
        ) { client!!.setScreen(SelectWorldScreen(this)) }
        addButtonWidget(
            TranslatableText("menu.features"), TranslatableText("button.features.description"),
            SETTINGS, 5, 60, 140,
            25, ColorHelper.changeAlpha(Color.YELLOW.rgb, 0.5f), 16, 16
        ) { }
        addButtonWidget(
            TranslatableText("menu.options"), TranslatableText("button.settings.description"),
            SETTINGS, 5, 87, 140,
            25, ColorHelper.changeAlpha(Color.ORANGE.rgb, 0.5f), 16, 16
        ) { client!!.setScreen(OptionsScreen(this, client!!.options)) }
        addButtonWidget(
            TranslatableText("menu.quit"), TranslatableText("button.exit.description"),
            CLOSE_GAME, 5, 114, 140,
            25, ColorHelper.changeAlpha(Color.RED.rgb, 0.5f), 16, 16
        ) { client!!.scheduleStop() }

        /* Buttons at the bottom of the bar in the screen */
        val multiplayer = multiplayerScreen as IMultiplayerScreenMixin
        addButtonWidget(
            TranslatableText("selectServer.direct"), LiteralText.EMPTY,
            PLAY_SINGLEPLAYER, 5, height - 5 - 77, 140, 13,
            ColorHelper.changeAlpha(0x69FFF1, 0.5f), 8, 8
        ) {
            multiplayer.selectedEntry = ServerInfo(I18n.translate("selectServer.defaultName"), "", false)
            client!!.setScreen(
                DirectConnectScreen(
                    this,
                    { confirmedAction: Boolean -> multiplayer.doDirectConnect(confirmedAction) },
                    multiplayer.selectedEntry
                )
            )
        }
        addButtonWidget(
            TranslatableText("selectServer.add"), LiteralText.EMPTY,
            ADD_SERVER, 5, height - 5 - 61, 140, 13,
            ColorHelper.changeAlpha(0x63D471, 0.5f), 8, 8
        ) {
            multiplayer.selectedEntry = ServerInfo(I18n.translate("selectServer.defaultName"), "", false)
            client!!.setScreen(
                AddServerScreen(
                    this,
                    { confirmedAction: Boolean -> multiplayer.doAddEntry(confirmedAction) },
                    multiplayer.selectedEntry
                )
            )
        }
        editButton = addButtonWidget(
            TranslatableText("selectServer.edit"), LiteralText.EMPTY,
            EDIT_SERVER, 5, height - 5 - 45, 140, 13,
            ColorHelper.changeAlpha(0x63A46C, 0.5f), 8, 8
        ) {
            val entry = Objects.requireNonNull(multiplayer.entryList)!!.selectedOrNull
            if (entry is ServerEntry) {
                val serverInfo = entry.server
                multiplayer.selectedEntry = ServerInfo(serverInfo.name, serverInfo.address, false)
                Objects.requireNonNull(multiplayer.selectedEntry)!!.copyFrom(serverInfo)
                client!!.setScreen(
                    AddServerScreen(
                        this,
                        { confirmedAction: Boolean -> multiplayer.doEditEntry(confirmedAction) },
                        multiplayer.selectedEntry
                    )
                )
            }
        }
        deleteButton = addButtonWidget(
            TranslatableText("selectServer.delete"), LiteralText.EMPTY,
            DELETE_SERVER, 5, height - 5 - 29, 140,
            13, ColorHelper.changeAlpha(0x6A7152, 0.5f), 8, 8
        ) {
            val entry = Objects.requireNonNull(multiplayer.entryList)!!.selectedOrNull
            if (entry is ServerEntry) {
                val string = entry.server.name
                if (string != null) {
                    client!!.setScreen(
                        ConfirmScreen(
                            { confirmedAction: Boolean -> multiplayer.doDeleteEntry(confirmedAction) },
                            TranslatableText("selectServer.deleteQuestion"),
                            TranslatableText("selectServer.deleteWarning", string),
                            TranslatableText("selectServer.deleteButton"),
                            ScreenTexts.CANCEL
                        )
                    )
                }
            }
        }
        addButtonWidget(
            TranslatableText("selectServer.refresh"), LiteralText.EMPTY, PLAY_SINGLEPLAYER,
            5, height - 5 - 13, 140, 13, ColorHelper.changeAlpha(0x233329, 0.5f),
            8, 8
        ) { multiplayer.refreshScreen() }
    }

    override fun tick() {
        editButton!!.active = (multiplayerScreen as IMultiplayerScreenMixin).deleteButton!!.active
        deleteButton!!.active = editButton!!.active
        val multiplayer = multiplayerScreen as IMultiplayerScreenMixin
        if (multiplayer.lanServerEntryList!!.needsUpdate()) {
            val list = multiplayer.lanServerEntryList!!.servers
            multiplayer.lanServerEntryList!!.markClean()
            multiplayer.entryList!!.setLanServers(list)
        }
        multiplayer.serverListPinger!!.tick()
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        multiplayerScreen.keyPressed(keyCode, scanCode, modifiers)
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        multiplayerScreen.mouseReleased(mouseX, mouseY, button)
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        multiplayerScreen.mouseClicked(mouseX, mouseY, button)
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        multiplayerScreen.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        multiplayerScreen.mouseScrolled(mouseX, mouseY, amount)
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        multiplayerScreen.mouseMoved(mouseX, mouseY)
        super.mouseMoved(mouseX, mouseY)
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(matrices)
        fill(matrices, 0, 0, 150, height, ColorHelper.changeAlpha(Color.BLACK.rgb, 0.75f))
        matrices.push()
        matrices.translate(SCREEN_MOVE_FOR_MULTIPLAYER.toDouble(), 0.0, 0.0)
        width += SCREEN_MOVE_FOR_MULTIPLAYER
        multiplayerScreen.render(matrices, mouseX - SCREEN_MOVE_FOR_MULTIPLAYER, mouseY, delta)
        width -= SCREEN_MOVE_FOR_MULTIPLAYER
        matrices.pop()

        RenderSystem.setShaderTexture(0, LOGO)
        val height = textRenderer.fontHeight * 3
        drawTexture(matrices, 1, 5, 0f, 0f, height, height, height, height)
        matrices.push()
        matrices.scale(1.9f, 1.9f, 1.9f)
        textRenderer.draw(matrices, Text.of("Jupiter Mod"), 16f, 5f, Color.WHITE.rgb)
        matrices.pop()
        super.render(matrices, mouseX, mouseY, delta)
    }

    companion object {
        @JvmStatic private val LOGO = Identifier(JupiterClient::class.simpleName?.lowercase(), "textures/logo/1024x1024.png")
        @JvmStatic private val PLAY_SINGLEPLAYER = Identifier(JupiterClient::class.simpleName?.lowercase(), "textures/buttons/play.png")
        @JvmStatic private val SETTINGS = Identifier(JupiterClient::class.simpleName?.lowercase(), "textures/buttons/settings.png")
        @JvmStatic private val CLOSE_GAME = Identifier(JupiterClient::class.simpleName?.lowercase(), "textures/buttons/back.png")
        @JvmStatic private val ADD_SERVER = Identifier(JupiterClient::class.simpleName?.lowercase(), "textures/buttons/add.png")
        @JvmStatic private val DELETE_SERVER = Identifier(JupiterClient::class.simpleName?.lowercase(), "textures/buttons/delete.png")
        @JvmStatic private val EDIT_SERVER = Identifier(JupiterClient::class.simpleName?.lowercase(), "textures/buttons/edit.png")
        const val SCREEN_MOVE_FOR_MULTIPLAYER = 115 / 3
    }

}