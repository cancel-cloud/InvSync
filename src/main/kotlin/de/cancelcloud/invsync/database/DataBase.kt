package de.cancelcloud.invsync.database

import de.cancelcloud.invsync.InvSyncerClass
import de.cancelcloud.invsync.utils.FileConfig
import java.io.File
import java.io.IOException

object DataBase {

    private var file: File = File("config/mysql.yml")
    private var config: FileConfig


    init {
        if (!file.exists()) {
            try {

                //file.createNewFile()
                config = FileConfig("mysql.yml")

                config.options().copyDefaults(true)
                config.addDefault("username", "banana")
                config.addDefault("password", "ilovebananas")
                config.addDefault("databasename", "minions")
                config.addDefault("address", "molehills")
                config.addDefault("port", "harbour")
                config.saveConfig()

                println("")
                println("")
                println("See Config Folder and then mysql.yml and change it!")
                println("")
                println("")

                InvSyncerClass.instance.server.shutdown()


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        config = FileConfig("mysql.yml")

    }
    /*
    /**
     * Database username.
     */
    val username = config.getString("username")

    /**
     * Database password.
     */
    val password = config.getString("password")

    /**
     * Database name.
     */
    val database = config.getString("databasename")

    /**
     * Database address.
     */
    val address = config.getString("address")

    /**
     * Database port.
     */
    val port = config.getInt("port")


     */

}