package io.github.keritial.keritize.spigot.command.tpa

import io.github.keritial.keritize.spigot.command.CommandWrapper
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TpaCommand(private val service: TpaService, private val reversed: Boolean = false) :
    CommandWrapper(requirePlayer = true, argumentsCount = 1) {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val requester = sender as Player
        val id = strings[0]
        val acceptor = Bukkit.getPlayer(id)
        if (acceptor == null) {
            sender.sendMessage("玩家 $id 不存在。")
            return true
        }
        if (acceptor == requester) {
            sender.sendMessage("喵，不用传送自己吧？")
            return false
        }

        service.request(requester, acceptor, reversed)

        return true
    }
}