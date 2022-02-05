package de.cancelcloud.invsync.utils

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class FileConfig(fileName: String) : YamlConfiguration() {
    private var seperator: String?
    private val path: String
    fun saveConfig() {
        try {
            save(path)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    init {
        seperator = System.getProperty("file.seperator")
        if (seperator == null) {
            seperator = "/"
        }
        path = "config/$fileName"
        val file = File(path)
        try {
            if (!file.exists()) {
                println("Creating new file $fileName")
                file.createNewFile()
            }
            load(path)
        } catch (e: IOException) {
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }
}