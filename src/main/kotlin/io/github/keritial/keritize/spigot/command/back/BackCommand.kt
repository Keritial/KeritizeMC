package io.github.keritial.keritize.spigot.command.back

import io.github.keritial.keritize.spigot.command.CommandWrapper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.Instant

class BackCommand(private val lastLocationService: LastLocationService) : CommandWrapper(requirePlayer = true) {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val player = sender as Player
        val record = lastLocationService.fetch(player)
//        if (Duration.between(Instant.now(), record.instant).abs().mi)
        if (record == null) {
            player.sendMessage("无记录的位置。")
            return true
        }
        if (Instant.now().epochSecond - record.instant.epochSecond < 1) {
            player.sendMessage("操作冷却中。")
            return true
        }
        player.sendMessage("正在返回上次的位置。")
        player.teleport(record.location)
        return true
    }
}