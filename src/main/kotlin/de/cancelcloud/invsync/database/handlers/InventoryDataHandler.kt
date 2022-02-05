package de.cancelcloud.invsync.database.handlers

import de.cancelcloud.invsync.InvSyncerClass
import de.cancelcloud.invsync.database.MySQL
import de.cancelcloud.invsync.utils.Base64.itemStackArrayToBase64
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.sql.ResultSet
import java.sql.SQLException

class InventoryDataHandler(ctl: InvSyncerClass) {

    var cutils: InvSyncerClass = ctl


    fun fromDataBase(player: Player): List<Any> {


        try {
            val stmt = MySQL.prepareStatement("select * from inventory WHERE player_uuid = '${player.uniqueId}'")
            val rs: ResultSet? = stmt!!.executeQuery()
            return if (rs!!.next()) {
                listOf<Any>(
                    rs.getString("player_uuid"),
                    rs.getString("player_name"),
                    rs.getString("inventory"),
                    rs.getString("armor"),
                    rs.getInt("hotbar_slot"),
                    rs.getInt("gamemode")
                )
            } else {
                emptyList()
            }


        } catch (e: SQLException) {
            println("could not get data from user: ${player.uniqueId}")
            player.kickPlayer("Please rejoin or conntact staff!")
        }

        return listOf<Any>()
    }


    fun fromDataBase(player: OfflinePlayer): List<Any> {


        try {
            val stmt = MySQL.prepareStatement("select * from inventory WHERE player_uuid = '${player.uniqueId}'")
            val rs: ResultSet? = stmt!!.executeQuery()
            return if (rs!!.next()) {
                listOf<Any>(
                    rs.getString("player_uuid"),
                    rs.getString("player_name"),
                    rs.getString("inventory"),
                    rs.getString("armor"),
                    rs.getInt("hotbar_slot"),
                    rs.getInt("gamemode")
                )
            } else {
                emptyList()
            }

        } catch (e: SQLException) {
            println("could not get data from user: ${player.uniqueId}")
        }
        return listOf<Any>()
    }


    fun toDataBase(player: Player, insert: Boolean): Boolean {
        val uuid = player.uniqueId
        val name = player.name
        val selectedSlot = player.inventory.heldItemSlot
        val mode: String
        if (insert) mode = "INSERT"
        else mode = "REPLACE"

        try {
            val stmt = MySQL.prepareStatement(
                """
                    $mode INTO inventory(player_uuid, player_name, inventory, armor, hotbar_slot, gamemode) 
                    VALUES 
                    (?, ?, ?, ?, ?, ?)
                """.trimIndent()
            )
            stmt!!.setString(1, uuid.toString())
            stmt.setString(2, name)
            stmt.setString(3, itemStackArrayToBase64(player.inventory.contents as Array<ItemStack>))
            stmt.setString(4, itemStackArrayToBase64(player.inventory.armorContents as Array<ItemStack>))
            stmt.setInt(5, selectedSlot)
            stmt.setInt(6, getPlayerGamemode(player))
            stmt.execute()
            return true
        } catch (e: SQLException) {
            println("Could not insert Data into Databse for player $name [$uuid]. \nStacktrace: \n $e")
        }
        return false
    }

    fun getPlayerGamemode(player: Player): Int {
        return when (player.gameMode) {
            GameMode.SURVIVAL -> 0
            GameMode.CREATIVE -> 1
            GameMode.ADVENTURE -> 2
            GameMode.SPECTATOR -> 3
        }
    }

    fun checkPlayer(player: Player): Boolean {
        val stmt = MySQL.prepareStatement("select * from inventory WHERE player_uuid = '${player.uniqueId}'")
        val rs: ResultSet? = stmt!!.executeQuery()

        return rs!!.next()
    }

}