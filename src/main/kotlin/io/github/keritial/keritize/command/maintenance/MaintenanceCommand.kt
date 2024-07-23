package io.github.keritial.keritize.command.maintenance

import io.github.keritial.keritize.command.CommandWrapper
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class MaintenanceCommand : CommandWrapper(), Listener {
    private var isUnderMaintenance = false
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        isUnderMaintenance = !isUnderMaintenance
        if (isUnderMaintenance) {
            Bukkit.broadcast(Component.text("服务器进入维护模式，新玩家将无法进入。详细信息请见群。"))
        } else {
            Bukkit.broadcast(Component.text("服务器解除维护模式，新玩家得以进入。"))
        }
        return true
    }

    @EventHandler
    fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        if (!isUnderMaintenance) return
        event.disallow(
            AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
            Component.text("服务器正在维护，请稍后再尝试进入。详细信息请见群。")
        )
    }
}