package de.cacheoverflow.jupiterclient.api.ui

import de.cacheoverflow.jupiterclient.api.ui.widgets.JupiterButtonWidget
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ButtonWidget.PressAction
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

open class ScreenFacade(title: Text) : Screen(title) {

    private var escapeScreen: Screen? = null

    constructor(text: String) : this(TranslatableText(text)) {}

    protected fun addTextFieldWidget(text: Text, x: Int, y: Int, width: Int, height: Int) {
        addDrawable(TextFieldWidget(textRenderer, x, y, width, height, text))
    }

    protected fun addTextFieldWidget(text: String, x: Int, y: Int, width: Int, height: Int) {
        this.addTextFieldWidget(Text.of(text), x, y, width, height)
    }

    protected fun addButtonWidget(
        text: Text, description: Text, image: Identifier,
        x: Int, y: Int, width: Int, height: Int,
        color: Int, imageWidth: Int, imageHeight: Int, onPress: PressAction?
    ): ButtonWidget {
        assert(client != null)
        return addDrawableChild(
            JupiterButtonWidget(
                client!!,
                text,
                description,
                image,
                imageWidth,
                imageHeight,
                x,
                y,
                width,
                height,
                color,
                onPress!!
            )
        )
    }

    protected fun escape() {
        checkNotNull(client) { "Minecraft Client is null" }
        client!!.setScreen(escapeScreen)
    }

    protected fun escape(buttonWidget: ButtonWidget?) {
        this.escape()
    }

    override fun close() {
        this.escape()
    }

    protected fun setEscapeScreen(escapeScreen: Screen) {
        this.escapeScreen = escapeScreen
    }

    protected fun getEscapeScreen(): Screen {
        return escapeScreen!!
    }
}