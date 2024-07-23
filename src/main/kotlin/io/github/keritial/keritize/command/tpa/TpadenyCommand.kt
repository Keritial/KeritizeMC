package io.github.keritial.keritize.command.tpa

import io.github.keritial.keritize.command.CommandWrapper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TpadenyCommand(private val tpaService: TpaService) : CommandWrapper(requirePlayer = true) {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val player = sender as Player
        player.sendMessage(
            if (tpaService.deny(player)) {
                "请求已拒绝。"
            } else {
                "没有请求。"
            }
        )

        return true
    }
}