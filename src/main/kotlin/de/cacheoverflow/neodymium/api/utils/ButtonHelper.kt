package de.cacheoverflow.neodymium.api.utils

import de.cacheoverflow.neodymium.injections.interfaces.client.gui.screen.IScreenMixin
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import java.util.function.Predicate

class ButtonHelper private constructor() {

    init {
        throw IllegalStateException("This class is not intended to be instantiated!")
    }

    companion object {
        @JvmStatic fun centerButtons(
            screen: Screen, yOffset: Int, screenWidth: Int,
            screenHeight: Int, buttonsPerRow: Int, xDistancePerButton: Int,
            yDistancePerButton: Int
        ) {
            centerButtons(
                screen,
                0,
                yOffset,
                screenWidth,
                screenHeight,
                buttonsPerRow,
                xDistancePerButton,
                yDistancePerButton
            )
        }

        @JvmStatic fun centerButtons(
            screen: Screen, xOffset: Int, yOffset: Int, screenWidth: Int,
            screenHeight: Int, buttonsPerRow: Int, xDistancePerButton: Int,
            yDistancePerButton: Int
        ) {
            centerButtons0<ButtonWidget>(
                ArrayList((screen as IScreenMixin).widgets.stream().filter(
                    Predicate { widget: Drawable? -> widget is ButtonWidget })
                    .map { widget: Drawable? -> widget as ButtonWidget }.toList()
                ),
                xOffset, yOffset, screenWidth, screenHeight, buttonsPerRow, xDistancePerButton, yDistancePerButton
            )
        }

        @JvmStatic fun <T : ButtonWidget?> centerButtons0(
            buttons: MutableList<ButtonWidget>, xOffset: Int, yOffset: Int, screenWidth: Int,
            screenHeight: Int, buttonsPerRow: Int, xDistancePerButton: Int,
            yDistancePerButton: Int
        ) {
            val row: MutableList<ButtonWidget> = ArrayList()
            var width = 0
            var height = 0
            var maxHeight = 0
            var y = 0
            for (i in 0 until buttonsPerRow) buttons.add(
                ButtonWidget(
                    0,
                    0,
                    -xDistancePerButton,
                    -yDistancePerButton,
                    null,
                    null
                )
            )
            for (i in buttons.indices) {
                if (i % buttonsPerRow == 0) height += buttons[i].height + yDistancePerButton
            }
            y = screenHeight / 2 - height / 2
            for (i in buttons.indices) {
                val button = buttons[i]
                if (i % buttonsPerRow == 0) {
                    width -= xDistancePerButton
                    var x = screenWidth / 2 - width / 2 + xOffset
                    for (j in row.indices) {
                        row[j].x = x
                        row[j].y = y + yOffset + yDistancePerButton / 2
                        x += row[j].width + xDistancePerButton
                        maxHeight = Math.max(maxHeight, row[j].height)
                        if (j == row.size - 1) y += maxHeight + yDistancePerButton
                    }
                    width = 0
                    row.clear()
                }
                row.add(button)
                width += button.width + xDistancePerButton
            }
            for (i in 0 until buttonsPerRow) buttons.removeAt(buttons.size - 1)
        }

        @JvmStatic fun getWidgets(screen: Screen): MutableList<Drawable?> {
            return (screen as IScreenMixin).widgets
        }

        @JvmStatic fun clearButtons(screen: Screen) {
            getWidgets(screen).removeIf(Predicate { widget: Drawable? -> widget is ButtonWidget })
        }
    }
}