package io.github.keritial.keritize.spigot.command

import io.github.keritial.keritize.spigot.humanReadableToBoolean
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

class KeritizeCommand(private val logger: Logger, private val plugin: Plugin):CommandWrapper() {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        if (strings.isEmpty()) {
            return false
        }
        return when (strings[0]) {
            "reload","r"-> reload()
            "disable","d"-> disable()
            "enable","e"-> enable()
            else -> false
        }
    }
    private fun reload(): Boolean {
        logger.warning("Reloading...")
        plugin.reloadConfig()

        return true
    }
    private fun disable(): Boolean {
        logger.warning("Disabling...")
        plugin.server.pluginManager.disablePlugin(plugin)

        return true
    }
    private fun enable(): Boolean {
        logger.warning("Enabling...")
        plugin.server.pluginManager.enablePlugin(plugin)

        return true
    }
}