package de.cancelcloud.invsync.database

import de.cancelcloud.invsync.utils.FileConfig
import lombok.extern.slf4j.Slf4j
import org.intellij.lang.annotations.Language
import java.io.File
import java.sql.*
import java.util.function.Consumer


@Slf4j
object MySQL {

    private var connection: Connection? = null
    private var config: FileConfig = FileConfig("mysql.yml")

    fun onConnect() {
        val file = File("config/mysql.yml")
        if (!file.exists()) {
            DataBase
            return
        }

        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://" + config.getString("address") + ":${config.getInt("port")}/" + config.getString("databasename") + "?autoReconnect=true",
                config.getString("username"), config.getString("password")
            )
            println("MySQL -> Connected.")


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }

        try {
            @Language("MySQL")
            val stmt = prepareStatement(
                """
                create table if not exists inventory
(
    id          int unsigned auto_increment
        primary key,
    player_uuid char(36)    not null,
    player_name varchar(16) not null,
    inventory   longtext    not null,
    armor       text        not null,
    hotbar_slot int(2)      not null,
    gamemode    int(1)      not null,
    constraint mpdb_inventory_player_uuid_uindex
        unique (player_uuid)
);
                """.trimIndent()
            )

            stmt!!.execute()
        } catch (e: SQLException) {
            println("Creation of table failed, please contact me via Github and paste Stacktrace \nStacktrace: \n $e")
        }
    }

    fun onDisconnect() {
        if (connection != null) {
            try {
                connection!!.close()
                println("MySQL -> Disconnected.")
            } catch (ex: SQLException) {
                ex.printStackTrace()
            }
        }
    }

    fun onReConnect() {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://" + config.getString("address") + ":${config.getInt("port")}/" + config.getString("databasename") + "?autoReconnect=true",
                config.getString("username"), config.getString("password")
            )
            println("MySQL -> Connected.")
        } catch (e: SQLException) {
            // e.printStackTrace();
            System.err.println("MySQL-Verbindung konnte nicht hergestellt werden!")
            System.err.println("MySQL wurde deaktiviert")
        }
    }

    fun onReConnectUpdate(update: String) {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://" + config.getString("address") + ":${config.getInt("port")}/" + config.getString("databasename") + "?autoReconnect=true",
                config.getString("username"), config.getString("password")
            )
            println("MySQL -> Connected.")
            onUpdate(update)
        } catch (e: SQLException) {
            // e.printStackTrace();
            System.err.println("MySQL-Verbindung konnte nicht hergestellt werden!")
            System.err.println("MySQL wurde deaktiviert")
        }
    }

    fun onReConnectQuery(update: String): ResultSet? {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://" + config.getString("address") + ":${config.getInt("port")}/" + config.getString("databasename") + "?autoReconnect=true",
                config.getString("username"), config.getString("password")
            )
            println("MySQL -> Connected.")
            return onReConnectQuery(update)
        } catch (e: SQLException) {
            // e.printStackTrace();
            System.err.println("MySQL-Verbindung konnte nicht hergestellt werden!")
            System.err.println("MySQL wurde deaktiviert")
        }
        return null
    }

    fun prepareStatement(@Language("sql") sql: String): PreparedStatement? {
        try {
            return connection!!.prepareStatement(sql)
        } catch (ex: SQLException) {
            println("Preparestatement spit out an exception: \n$ex")
        }
        return null
    }

    fun onUpdate(qry: String) {
        try {
            val stmt = connection!!.createStatement()
            stmt.executeUpdate(qry)
        } catch (ex: SQLException) {
            println("Preparestatement spit out an exception: \n$ex")
        }
    }

    fun onUpdate(qry: String, callback: Consumer<Statement?>) {
        try {
            val stmt = connection!!.createStatement()
            stmt.executeUpdate(qry)
            callback.accept(stmt)
        } catch (ex: SQLException) {
            println("Preparestatement spit out an exception: \n$ex")
        }
    }

    fun onQuery(qry: String): ResultSet? {
        var set: ResultSet? = null
        try {
            val stmt = connection!!.createStatement()
            set = stmt.executeQuery(qry)
        } catch (ex: SQLException) {
            println("Preparestatement spit out an exception: \n$ex")
        }
        return set
    }

    fun onQuery(qry: String, callback: Consumer<ResultSet>) {
        try {
            val stmt = connection!!.createStatement()
            val set = stmt.executeQuery(qry)
            callback.accept(set)
        } catch (ex: SQLException) {
            println("Preparestatement spit out an exception: \n$ex")
        }
    }

    fun getLastID(tableName: String): Int {
        val query = onQuery("SELECT ID AS LastID FROM $tableName WHERE ID = @@Identity;")
        query?.let {
            if (it.next()) {
                return it.getInt("LastID")
            }
        }
        return -1
    }

}