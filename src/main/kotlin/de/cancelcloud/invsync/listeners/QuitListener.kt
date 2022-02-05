package de.cancelcloud.invsync.listeners

import de.cancelcloud.invsync.InvSyncerClass
import de.cancelcloud.invsync.database.handlers.InventoryDataHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitListener(var invsync: InvSyncerClass) : Listener {
    private val handler: InventoryDataHandler = InventoryDataHandler(InvSyncerClass.instance)


    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player

        if (!handler.checkPlayer(player)) {
            handler.toDataBase(player, true)
        } else {
            handler.toDataBase(player, false)
            player.inventory.clear()
        }
    }
}