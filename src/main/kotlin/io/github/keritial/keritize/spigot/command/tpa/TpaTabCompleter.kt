package io.github.keritial.keritize.spigot.command.tpa

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TpaTabCompleter : TabCompleter {
    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>
    ): MutableList<String> {
        return if (p3.size == 1) {
            Bukkit.getOnlinePlayers().filter { it != p0 }.map { it.name }.toMutableList()
        } else ArrayList()
    }
}