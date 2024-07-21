package io.github.keritial.keritize.command

// import org.bukkit.entity.Skeleton
import org.bukkit.entity.Creeper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
//import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityExplodeEvent

class EventTaker : Listener {
    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        if (event.entity is Creeper) {
            val blockList = event.blockList()
            blockList.removeAll(blockList)
        }
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