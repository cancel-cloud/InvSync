package de.cancelcloud.invsync.utils

import org.bukkit.GameMode
import org.bukkit.entity.Player

object PlayerUtils {

    fun getPlayerGamemode(player: Player): Int {
        return when (player.gameMode) {
            GameMode.SURVIVAL -> 0
            GameMode.CREATIVE -> 1
            GameMode.ADVENTURE -> 2
            GameMode.SPECTATOR -> 3
        }
    }

}