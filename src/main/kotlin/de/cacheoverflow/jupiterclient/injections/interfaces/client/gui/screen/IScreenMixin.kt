package de.cacheoverflow.jupiterclient.injections.interfaces.client.gui.screen

import net.minecraft.client.gui.Drawable

interface IScreenMixin {

    val widgets: MutableList<Drawable?>

}