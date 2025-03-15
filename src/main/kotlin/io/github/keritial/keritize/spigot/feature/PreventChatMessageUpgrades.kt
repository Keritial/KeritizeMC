package io.github.keritial.keritize.spigot.feature

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import io.github.keritial.keritize.spigot.displayPlayer
import io.github.keritial.keritize.spigot.getManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

class PreventChatMessageUpgrades(private val logger: Logger, private val plugin: Plugin) : Listener {
    fun preventChatSessionUpdate() {
//        if (!isProtocolLibInstalled()) {
//            logger.warning("ProtocolLib is not installed, skipping packet adapter registration.")
//            return
//        }
//        val manager = ProtocolLibrary.getProtocolManager()
        val manager = getManager()
        if (manager == null) {
            logger.warning("ProtocolLib is not installed, skipping packet adapter registration.")

            return
        }
        manager.addPacketListener(object :
            PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT_SESSION_UPDATE) {
            override fun onPacketReceiving(event: PacketEvent) {
                val player = event.player
                logger.info("Filtered CHAT_SESSION_UPDATE packet for ${displayPlayer(player)}.")
                player.sendMessage("已阻止聊天签名。")
                event.isCancelled = true
            }
        })
        logger.info("Filtering CHAT_SESSION_UPDATE packets.")
    }


    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
//        logger.info("${event.reason} ${event.player.name}")
        if (!event.reason.contains("Invalid signature for profile public key.")) {
            return
        }
        val player = event.player
        logger.info("Chat signature validation failed for ${displayPlayer(player)}, preventing them from being kicked.")
        event.isCancelled = true
    }
}