package de.cancelcloud.invsync.listeners

import de.cancelcloud.invsync.InvSyncerClass
import de.cancelcloud.invsync.database.handlers.GetSettingsValueHandler
import de.cancelcloud.invsync.database.handlers.InventoryDataHandler
import de.cancelcloud.invsync.utils.Base64.itemStackArrayFromBase64
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {

    private val handler: InventoryDataHandler = InventoryDataHandler(InvSyncerClass.instance)


    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        if (player.hasPlayedBefore()) {
            val playerData = handler.fromDataBase(player)

            if (!player.inventory.isEmpty) {
                player.inventory.contents = itemStackArrayFromBase64(playerData[2].toString())
            }

            player.inventory.heldItemSlot = playerData[4] as Int
            player.gameMode = when (playerData[5]) {
                0 -> GameMode.SURVIVAL
                1 -> GameMode.CREATIVE
                2 -> GameMode.ADVENTURE
                3 -> GameMode.SPECTATOR
                else -> GameMode.SURVIVAL
            }

        } else {
            GetSettingsValueHandler(InvSyncerClass.instance).setvalue(player, 1)
        }


    }
}