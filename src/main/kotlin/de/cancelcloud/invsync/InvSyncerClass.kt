package de.cancelcloud.invsync

import de.cancelcloud.invsync.database.MySQL.onConnect
import de.cancelcloud.invsync.database.MySQL.onDisconnect
import de.cancelcloud.invsync.listeners.JoinListener
import de.cancelcloud.invsync.listeners.QuitListener
import de.cancelcloud.invsync.utils.Config
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class InvSyncerClass : JavaPlugin() {
    private lateinit var configuration: Config


    init {
        instance = this
    }

    override fun onLoad() {
        onConnect()
        configuration = Config()
    }

    override fun onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE.toString() + "InvSync Loaded")

        with(Bukkit.getPluginManager()) {
            registerEvents(JoinListener(), this@InvSyncerClass)
            registerEvents(QuitListener(this@InvSyncerClass), this@InvSyncerClass)
        }
    }


    override fun onDisable() {
        // Plugin shutdown logic
        onDisconnect()
    }


    companion object {
        lateinit var instance: InvSyncerClass
    }


}