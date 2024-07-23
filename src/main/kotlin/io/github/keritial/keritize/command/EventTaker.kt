package io.github.keritial.keritize.command

// import org.bukkit.entity.Skeleton
//import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.Material
import org.bukkit.entity.Creeper
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.logging.Logger

class EventTaker(private val logger: Logger) : Listener {
    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        if (event.entity is Creeper) {
            val blockList = event.blockList()
            blockList.removeAll(blockList)
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.player
        val killer = player.killer
        if (killer != null && player.killer is Player) {
            logger.info("${player.name} is killed by ${killer.name}")
            val skull = ItemStack(Material.PLAYER_HEAD)
            val metadata = skull.itemMeta as SkullMeta
            metadata.setOwningPlayer(player)
            skull.itemMeta = metadata
            player.world.dropItem(player.location, skull)
        }
//        if (player.uniqueId == UUID.fromString("a414c775-d4db-4e06-9cab-2f85b96a721c")) {
//            if (player.killer?.uniqueId != UUID.fromString("938669e1-f86e-4426-b011-2418cbdaea3f")) {
//                logger.info("Cancelling ${player.name}'s death.")
//                event.isCancelled = true
//                return
//            }
//        }
    }

//    @EventHandler
//    fun fuckMob(event: CreatureSpawnEvent) {
//        if (event.entity is Creeper || event.entity is Skeleton) {
//            val luckyNumber = (0..2).random()
//            if (luckyNumber == 1) {
//                return
//            }
//            event.isCancelled = true
//        }
//    }
}