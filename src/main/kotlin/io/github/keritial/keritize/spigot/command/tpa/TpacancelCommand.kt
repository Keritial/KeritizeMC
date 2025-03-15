package io.github.keritial.keritize.spigot.command.tpa

import io.github.keritial.keritize.spigot.command.CommandWrapper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TpacancelCommand(private val tpaService: TpaService) : CommandWrapper(requirePlayer = true) {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val player = sender as Player
        player.sendMessage(
            if (tpaService.cancel(player)) {
                "请求已取消。"
            } else {
                "没有请求。"
            }
        )

        return true
    }
}