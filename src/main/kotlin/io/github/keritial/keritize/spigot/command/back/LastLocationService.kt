package io.github.keritial.keritize.spigot.command.back

import io.github.keritial.keritize.spigot.displayCoordinates
import io.github.keritial.keritize.spigot.isLocationSafe
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import java.time.Instant
import java.util.*
import java.util.logging.Logger

class LastLocationService(private val logger: Logger) : Listener {
    private val locations = HashSet<LocationRecord>()

    fun record(player: Player, location: Location) {
        removePlayer(player)
        locations.add(LocationRecord(player, location, Instant.now()))
        val locationDisplay = displayCoordinates(location)
        logger.info("Player ${player.name} last location recorded: $locationDisplay")
        player.sendMessage(
            if (isLocationSafe(location)) {
                "位置 $locationDisplay 被记录。"
            } else {
                "上次被记录的位置 $locationDisplay 不安全。"
            }
        )
    }

    fun fetch(player: Player) = locations.find { it.player == player }

    fun removePlayer(player: Player) = locations.removeIf { it.player == player }

    fun recordPlayer(player: Player) {
        record(player, player.location)
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val cause = event.cause
        val player = event.player
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

            PlayerTeleportEvent.TeleportCause.COMMAND, PlayerTeleportEvent.TeleportCause.PLUGIN, PlayerTeleportEvent.TeleportCause.SPECTATE -> recordPlayer(
                player
            )

            else -> return
        }
        logger.info("Recorded player ${player.name} location for triggering PlayerTeleportEvent with ${cause.name}.")

    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        recordPlayer(player)
        logger.info("Recorded player ${player.name} location on death.")
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        removePlayer(event.player)
    }

}

data class LocationRecord(val player: Player, val location: Location, val instant: Instant)