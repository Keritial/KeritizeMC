package io.github.keritial.keritize.command.back

import io.github.keritial.keritize.formatCoordinates
import io.github.keritial.keritize.isLocationSafe
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.logging.Logger

class LastLocationService(private val logger: Logger) {
    private val locations = HashMap<Player, Location>()

    fun record(player: Player, location: Location) {
        removePlayer(player)
        locations[player] = location
        val locationDisplay = formatCoordinates(location)
        logger.info("Player ${player.name} last location recorded: $locationDisplay")
        player.sendMessage(if (isLocationSafe(location)){
            "位置 $locationDisplay 被记录。"
        } else {
            "上次被记录的位置 $locationDisplay 不安全。"
        })
    }

    fun fetch(player: Player) = locations[player]

    private fun removePlayer(player: Player) = locations.remove(player)

    fun recordPlayer(player: Player) {
        record(player, player.location)
    }

}