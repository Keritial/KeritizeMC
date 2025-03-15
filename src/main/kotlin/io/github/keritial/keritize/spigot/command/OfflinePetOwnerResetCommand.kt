package io.github.keritial.keritize.spigot.command

import io.github.keritial.keritize.spigot.generateOfflineUuid
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable
import java.util.logging.Logger

class OfflinePetOwnerResetCommand(private val logger: Logger): CommandWrapper(requirePlayer = true) {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val player = sender as Player
        val uuid = player.uniqueId

        val radius = 128.0
        val tameables = player.getNearbyEntities(radius,radius,radius).filterIsInstance<Tameable>().filter { it.isTamed }
        if (tameables.isEmpty()) {
            player.sendMessage("没有找到已驯服实体，请在已驯服实体半径 $radius 格之内使用这个指令。")
            return true
        }

        val migrated = tameables.filter { it.owner!==null&& it.owner!!.uniqueId == player.uniqueId }
        if (migrated.size==tameables.size) {
            player.sendMessage("半径 $radius 格之内的已驯服实体都是你的。")
            return true
        }

        val offlineUuid = generateOfflineUuid(player.name)

        val offlinePets = tameables.filter { it.owner!==null&& it.owner!!.uniqueId == offlineUuid }
        if (offlinePets.isEmpty()) {
            player.sendMessage("半径 $radius 格之内有 ${tameables.size} 已驯服实体，有 ${migrated.size} 个已是你的。")
            return true
        }
        logger.info("UUID of player ${player.name} is $uuid now, offline UUID should be $offlineUuid.")

        for (pet in offlinePets) {
            logger.info("Migrating pet ${pet.name} (${pet.uniqueId}), its original owner is ${pet.owner?.name} (${pet.owner?.uniqueId}).")
            pet.owner = player
            player.sendMessage("已将 ${pet.name} 的主人迁移为你。")
        }

        return true
    }

}
