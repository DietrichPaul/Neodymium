package de.cacheoverflow.jupiterclient.injections.interfaces.client.gui.screen.multiplayer

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.network.LanServerQueryManager.LanServerEntryList
import net.minecraft.client.network.MultiplayerServerListPinger
import net.minecraft.client.network.ServerInfo

interface IMultiplayerScreenMixin {

    fun doDirectConnect(confirmedAction: Boolean)

    fun doAddEntry(confirmedAction: Boolean)

    fun doEditEntry(confirmedAction: Boolean)

    fun doDeleteEntry(confirmedAction: Boolean)

    fun doConnect(serverInfo: ServerInfo)

    fun refreshScreen()

    fun getParentScreen(): Screen

    val entryList: MultiplayerServerListWidget?

    val lanServerEntryList: LanServerEntryList?

    val serverListPinger: MultiplayerServerListPinger?

    val deleteButton: ButtonWidget?

    var selectedEntry: ServerInfo?

}