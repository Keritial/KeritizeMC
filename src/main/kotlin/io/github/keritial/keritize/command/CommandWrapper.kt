package io.github.keritial.keritize.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

open class CommandWrapper(private val requirePlayer: Boolean = false, private val argumentsCount: Int = 0) : CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (this.requirePlayer && p0 !is Player) {
            p0.sendMessage("Only player could invoke this command.")
            return false
        }
        if (argumentsCount != 0 && argumentsCount > p3.size) {
            p0.sendMessage("Too few arguments, at least $argumentsCount required.")
            return false
        }
        return this.execute(p0, p1, p2, p3)
    }

    open fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean = false
}