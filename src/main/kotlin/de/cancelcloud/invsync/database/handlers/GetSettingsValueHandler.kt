package de.cancelcloud.invsync.database.handlers

import de.cancelcloud.invsync.InvSyncerClass
import de.cancelcloud.invsync.database.MySQL
import org.bukkit.entity.Player
import java.sql.ResultSet
import java.sql.SQLException

class GetSettingsValueHandler(ctl: InvSyncerClass) {

    var ctl: InvSyncerClass = ctl


    fun getsettingvalue(player: Player, columnLabel: String): Boolean {
        try {
            val stmt = MySQL.prepareStatement("SELECT * FROM settings WHERE player_uuid = '${player.uniqueId}'")
            val rs: ResultSet? = stmt!!.executeQuery()
            return if (rs!!.next()) {
                rs.getBoolean(columnLabel)
            } else {
                return false
            }
        } catch (e: SQLException) {
            println("could not get setting value for Player: ${player.uniqueId}\n${e.printStackTrace()}")
        }

        return false
    }


    fun setvalue(player: Player, toggle: Int) {
        try {
            val stmt = MySQL.prepareStatement("REPLACE INTO settings(player_uuid, mentioned) VALUES (?, ?)")
            stmt!!.setString(1, player.uniqueId.toString())
            stmt.setInt(2, toggle)
            stmt.execute()
        } catch (e: SQLException) {
            println("could not save setting value for Player: ${player.uniqueId}\n${e.printStackTrace()}")
        }
    }


}