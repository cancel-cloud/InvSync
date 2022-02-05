package de.cancelcloud.invsync.utils

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class Config {
    private val file: File
    val config: YamlConfiguration
    fun save() {
        try {
            config.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    init {
        val dir = File("./plugins/InvSync/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        file = File(dir, "config.yml")
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        config = YamlConfiguration.loadConfiguration(file)
    }
}