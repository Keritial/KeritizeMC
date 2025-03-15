package io.github.keritial.keritize.spigot.command.tpa

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.ArrayList

class TpacceptTabCompleter(private val tpaService: TpaService):TabCompleter {
    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>?
    ): MutableList<String> {
        val player = p0 as Player
        return if (p3 != null && p3.size==1) {
            tpaService.byAcceptor(player).map { it.requester.name }.toMutableList()
        } else ArrayList()
    }

}