package io.github.keritial.keritize.spigot.command.maintenance

import io.github.keritial.keritize.spigot.command.CommandWrapper
import io.github.keritial.keritize.spigot.displayPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

class MaintenanceCommand(private val plugin: Plugin, private val logger: Logger) : CommandWrapper(), Listener {
    private var isUnderMaintenance = false
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        isUnderMaintenance = !isUnderMaintenance
        if (isUnderMaintenance) {
            plugin.server.onlinePlayers.forEach { it.sendMessage("服务器进入维护模式，新玩家将无法进入；在线玩家还有一定时间保存进度，随后将被踢出。详细信息请见群。") }
        } else {
            plugin.server.onlinePlayers.forEach { it.sendMessage("服务器解除维护模式，新玩家得以进入。") }
        }
        return true
    }

    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        if (event.player.isOp) {
            if (
                event.result == PlayerLoginEvent.Result.KICK_FULL &&
                plugin.config.getBoolean("operator-bypass-player-cap", true)
            ) {
                logger.info("Though server is full, allowed server operator ${displayPlayer(event.player)} to join.")
                event.allow()
            }
            return
        }

        if (!isUnderMaintenance) {
            return
        }

        logger.info("Prevented ${displayPlayer(event.player)} from joining maintenance server.")

        event.disallow(
            PlayerLoginEvent.Result.KICK_OTHER,
            ("非常抱歉给您带来不便，服务器正在维护，请稍后再尝试进入，感谢您的理解和支持。详细信息请见群。")
        )
    }

//    @EventHandler
//    fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
//        if (event.)
//        if (!isUnderMaintenance) {
//            return
//        }
////        val player = plugin.server.getOfflinePlayer(event.uniqueId)
//        if (checkOp(event.uniqueId)) {
//            return
//        }
//    }

//    private fun checkOp(uuid:UUID): Boolean {
//        return plugin.server.getOfflinePlayer(uuid).isOp
//    }
}