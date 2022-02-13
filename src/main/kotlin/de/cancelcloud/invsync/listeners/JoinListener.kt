package de.cancelcloud.invsync.listeners

import de.cancelcloud.invsync.database.MySQL
import de.cancelcloud.invsync.utils.Base64.itemStackArrayFromBase64
import de.cancelcloud.invsync.utils.Base64.itemStackArrayToBase64
import de.cancelcloud.invsync.utils.PlayerUtils
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class JoinListener : Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        val checkPlayerExistsInDB = transaction { MySQL.InventoryTable.slice(MySQL.InventoryTable.player_uuid)
            .select(where = MySQL.InventoryTable.player_uuid eq player.uniqueId.toString()).count() }

        if (checkPlayerExistsInDB != 0L) {
            val inventoryTransaction = transaction {
                //addLogger(StdOutSqlLogger)
                MySQL.InventoryTable.slice(MySQL.InventoryTable.inventory)
                    .select(where = MySQL.InventoryTable.player_uuid eq player.uniqueId.toString()).first()[MySQL.InventoryTable.inventory]
            }
            val armorTransaction = transaction {
                //addLogger(StdOutSqlLogger)
                MySQL.InventoryTable.slice(MySQL.InventoryTable.armor)
                    .select(where = MySQL.InventoryTable.player_uuid eq player.uniqueId.toString()).first()[MySQL.InventoryTable.armor]
            }
            val hotbar_slotTransaction = transaction {
                //addLogger(StdOutSqlLogger)
                MySQL.InventoryTable.slice(MySQL.InventoryTable.hotbar_slot)
                    .select(where = MySQL.InventoryTable.player_uuid eq player.uniqueId.toString())
                    .first()[MySQL.InventoryTable.hotbar_slot]
            }
            val gameModeTransaction = transaction {
                //addLogger(StdOutSqlLogger)
                MySQL.InventoryTable.slice(MySQL.InventoryTable.gamemode)
                    .select(where = MySQL.InventoryTable.player_uuid eq player.uniqueId.toString()).first()[MySQL.InventoryTable.gamemode]
            }


            player.inventory.contents = itemStackArrayFromBase64(inventoryTransaction)


            player.inventory.heldItemSlot = hotbar_slotTransaction
            player.gameMode = when (gameModeTransaction) {
                0 -> GameMode.SURVIVAL
                1 -> GameMode.CREATIVE
                2 -> GameMode.ADVENTURE
                3 -> GameMode.SPECTATOR
                else -> GameMode.SURVIVAL
            }

            player.inventory.armorContents = itemStackArrayFromBase64(armorTransaction)

        } else {
            transaction {
                MySQL.InventoryTable.insert {
                    it[id] = null
                    it[player_uuid] = player.uniqueId.toString()
                    it[player_name] = player.name
                    it[inventory] = itemStackArrayToBase64(player.inventory.contents as Array<ItemStack>).toString()
                    it[armor] = itemStackArrayToBase64(player.inventory.armorContents as Array<ItemStack>).toString()
                    it[hotbar_slot] = player.inventory.heldItemSlot
                    it[gamemode] = PlayerUtils.getPlayerGamemode(player)
                }
            }
        }
    }
}