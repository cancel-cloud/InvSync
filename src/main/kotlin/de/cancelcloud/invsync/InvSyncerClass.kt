package de.cancelcloud.invsync

import de.cancelcloud.invsync.database.MySQL
import de.cancelcloud.invsync.listeners.JoinListener
import de.cancelcloud.invsync.listeners.QuitListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class InvSyncerClass : JavaPlugin() {

    init {
        instance = this
    }

    override fun onLoad() {
        MySQL.checkCredentialFile()
    }

    override fun onEnable() {
        MySQL.database
        transaction {
            SchemaUtils.create(MySQL.InventoryTable)
        }

        with(Bukkit.getPluginManager()) {
            registerEvents(JoinListener(), this@InvSyncerClass)
            registerEvents(QuitListener(this@InvSyncerClass), this@InvSyncerClass)
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE.toString() + "InvSync Loaded")
    }


    override fun onDisable() {
        // Plugin shutdown logic
    }


    companion object {
        lateinit var instance: InvSyncerClass
    }
}