package io.github.keritial.keritize.spigot.useless

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class BlankTabCompleter : TabCompleter {
//    override fun onTabComplete(
//        p0: CommandSender,
//        p1: Command,
//        p2: String,
//        p3: Array<out String>?
//    ): MutableList<String> {
//        return ArrayList()
//    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>
    ): MutableList<String> {
        return ArrayList()
    }
}