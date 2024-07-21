package io.github.keritial.keritize.command.tpa

import io.github.keritial.keritize.command.back.LastLocationService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerTeleportEvent
import java.util.logging.Logger

class LocationListener(private val logger: Logger, private val lastLocationService: LastLocationService) : Listener {
    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val cause = event.cause
        val player = event.player
        logger.info("Player ${player.name} triggered PlayerTeleportEvent for ${cause.name}")
        when (cause) {
            // PlayerTeleportEvent.TeleportCause.ENDER_PEARL -> return
            // PlayerTeleportEvent.TeleportCause.COMMAND -> return
            // PlayerTeleportEvent.TeleportCause.PLUGIN -> return
            // PlayerTeleportEvent.TeleportCause.NETHER_PORTAL -> return
            // PlayerTeleportEvent.TeleportCause.END_PORTAL -> return
            // PlayerTeleportEvent.TeleportCause.SPECTATE -> return
            // PlayerTeleportEvent.TeleportCause.END_GATEWAY -> return
            // PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT -> return
            // PlayerTeleportEvent.TeleportCause.DISMOUNT -> return
            // PlayerTeleportEvent.TeleportCause.EXIT_BED -> return
            // PlayerTeleportEvent.TeleportCause.UNKNOWN -> return

            PlayerTeleportEvent.TeleportCause.COMMAND,
            PlayerTeleportEvent.TeleportCause.PLUGIN,
            PlayerTeleportEvent.TeleportCause.SPECTATE
            -> lastLocationService.recordPlayer(player)

            else -> return
        }

    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        lastLocationService.recordPlayer(event.player)
    }
}