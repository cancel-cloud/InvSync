package de.cancelcloud.invsync.database

import de.cancelcloud.invsync.utils.FileConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import java.io.File
import java.sql.SQLException


object MySQL {

    private var config: FileConfig = FileConfig("mysql.yml")

    fun checkCredentialFile() {
        val file = File("config/mysql.yml")
        if (!file.exists()) {
            DataBase
            return
        }
    }

    val database by lazy {
        try {
            Database.connect(
                "jdbc:mysql://${config.getString("address")}:${config.getInt("port")}/${config.getString("databasename")}",
                driver = "com.mysql.cj.jdbc.Driver",
                user = config.getString("username")!!,
                password = config.getString("password")!!
            )
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }


    object InventoryTable : Table() {
        val id = integer("id").autoIncrement().nullable()
        val player_uuid = varchar("player_uuid", 36)
        val player_name = varchar("player_name", 36)
        val inventory = text("inventory")
        val armor = text("armor")
        val hotbar_slot = integer("hotbar_slot")
        val gamemode = integer("gamemode")

        override val primaryKey = PrimaryKey(id)
    }
}