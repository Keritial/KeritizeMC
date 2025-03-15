package io.github.keritial.keritize.spigot.feature

import io.github.keritial.keritize.spigot.displayPlayer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.logging.Logger

class DropPlayerSkull(private val logger: Logger):Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        val killer = player.killer
        if (killer == null || player.killer !is Player) {
            return
        }
        logger.info("${displayPlayer(player)} is killed by ${displayPlayer(killer)}.")

        val skull = ItemStack(Material.PLAYER_HEAD)
        val metadata = skull.itemMeta as SkullMeta
        metadata.setOwningPlayer(player)
        skull.itemMeta = metadata
        player.world.dropItem(player.location, skull)
//        if (player.uniqueId == UUID.fromString("a414c775-d4db-4e06-9cab-2f85b96a721c")) {
//            if (player.killer?.uniqueId != UUID.fromString("938669e1-f86e-4426-b011-2418cbdaea3f")) {
//                logger.info("Cancelling ${player.name}'s death.")
//                event.isCancelled = true
//                return
//            }
//        }
    }
}