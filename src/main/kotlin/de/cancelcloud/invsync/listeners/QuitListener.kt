package de.cancelcloud.invsync.listeners

import de.cancelcloud.invsync.InvSyncerClass
import de.cancelcloud.invsync.database.MySQL
import de.cancelcloud.invsync.utils.Base64
import de.cancelcloud.invsync.utils.PlayerUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class QuitListener(var invsync: InvSyncerClass) : Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player

        transaction {
            MySQL.InventoryTable.update {
                it[player_uuid] = player.uniqueId.toString()
                it[player_name] = player.name
                it[inventory] = Base64.itemStackArrayToBase64(player.inventory.contents as Array<ItemStack>).toString()
                it[armor] = Base64.itemStackArrayToBase64(player.inventory.armorContents as Array<ItemStack>).toString()
                it[hotbar_slot] = player.inventory.heldItemSlot
                it[gamemode] = PlayerUtils.getPlayerGamemode(player)
            }
        }
        player.inventory.clear()
    }
}