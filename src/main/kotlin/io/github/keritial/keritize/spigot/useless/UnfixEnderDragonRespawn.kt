package io.github.keritial.keritize.spigot.useless

import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPlaceEvent
import java.util.logging.Logger

class UnfixEnderDragonRespawn(private val logger: Logger):Listener {

    @EventHandler
    fun onEntityPlace(event: EntityPlaceEvent) {
        if (event.entityType !== EntityType.END_CRYSTAL) {
            return
        }
        val world = event.block.world
        if (world.environment !== World.Environment.THE_END) {
            return
        }
        try {
            val dragonFightField  = world.javaClass.getDeclaredField("dragonFight")
            dragonFightField.setAccessible(true)
            val endDragonFight = dragonFightField.get(world)
//            val fight = Class.forName("net.minecraft.world.level.dimension.end.EndDragonFight")
            val tryRespawn = endDragonFight.javaClass.getDeclaredMethod("tryRespawn")
            tryRespawn.isAccessible = true
            tryRespawn.invoke(endDragonFight,null)
        } catch (e: NoClassDefFoundError) {
            logger.warning("Server is running Paper and unfix-end-crystal-check is enabled, but failed to invoke tryRespawn.")
        }
        // https://github.com/PaperMC/Paper/commit/cdc3b28062aab8300059c28ef9262c94f366d32e#diff-148dec5539686fec9b94fe96dfcc1e0c88ddcbb8b22b67194a720df5cb681959R158

    }
}