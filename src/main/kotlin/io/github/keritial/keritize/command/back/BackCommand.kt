package io.github.keritial.keritize.command.back

import io.github.keritial.keritize.command.CommandWrapper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BackCommand(private val lastLocationService: LastLocationService): CommandWrapper(requirePlayer = true) {
    override fun execute(sender: CommandSender, command: Command, commandLiteral: String, strings: Array<out String>): Boolean {
        val player = sender as Player
        val location = lastLocationService.fetch(player)
        if (location == null) {
            player.sendMessage("无记录的位置。")
            return true
        }
        player.teleport(location)
        return true
    }
}