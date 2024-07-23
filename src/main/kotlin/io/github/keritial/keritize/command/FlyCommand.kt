package io.github.keritial.keritize.command

import io.github.keritial.keritize.humanReadableToBoolean
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand:CommandWrapper(requirePlayer = true) {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val player = sender as Player
        val argumentsCount = strings.size
        val currentStateReversed = !player.allowFlight
        val target = when (argumentsCount) {
            0,1 -> player
            else -> return false
        }
        val toState = when (argumentsCount) {
            0 -> currentStateReversed
            1 ->
                humanReadableToBoolean(strings[0]) ?: currentStateReversed

//            2 -> toBoolean(strings[1]) ?: return false
            else -> return false
        }
        target.allowFlight = toState
        return true
    }
}