@file:Suppress("DEPRECATION") // We have to use sync event here

package io.github.keritial.keritize

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.geysermc.geyser.api.GeyserApi
import java.util.logging.Logger

class GeyserAddon(private val geyserApi: GeyserApi, private val logger: Logger) : Listener {
    @EventHandler
    fun onChat(event: PlayerChatEvent) {
        val message = event.message
        val player = event.player
        if (
            !geyserApi.isBedrockPlayer(player.uniqueId) ||
            (!message.startsWith("!") && !message.startsWith("！"))
        ) {
            return
        }
        val command = message.substring(1)
        if (message.substring(0, 1) == message.substring(1, 2)) {
            event.message = command
        }
        event.isCancelled = true
        player.performCommand(command)
        logger.info("Player ${player.name} issued commmand /${command}")
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (geyserApi.isBedrockPlayer(player.uniqueId)) {
            player.sendMessage("因开启了 Xbox 成就，因此无法使用 / 作为前缀执行指令。如果需要执行指令，请使用 “!”（半角）或 “！”（全角） 感叹号作为前缀，如“!help” 。如果需要发送以感叹号开头的聊天消息，请打 2 个感叹号，例如“!!1”会变为“!1”")
        }
    }
}