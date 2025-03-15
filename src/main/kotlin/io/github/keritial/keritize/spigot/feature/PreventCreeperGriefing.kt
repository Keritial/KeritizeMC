package io.github.keritial.keritize.spigot.feature

import io.github.keritial.keritize.spigot.displayCoordinates
import org.bukkit.entity.Creeper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import java.util.logging.Logger

class PreventCreeperGriefing(private val logger: Logger):Listener {


    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
//        if (!config.getBoolean("prevent-creeper-griefing", false)) {
//            return
//        }
        val entity = event.entity
        if (entity !is Creeper) {
            return
        }
        val blockList = event.blockList()
        val size = blockList.size
        blockList.removeAll(blockList)
        val location = entity.location
        logger.info("Prevented creeper from griefing $size block(s) at ${displayCoordinates(location)}.")
    }
}
