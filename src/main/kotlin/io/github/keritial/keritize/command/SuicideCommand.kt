//@file:Suppress("UnstableApiUsage")

package io.github.keritial.keritize.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SuicideCommand : CommandWrapper(requirePlayer = true) {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val player = sender as Player
//        player.damage(Double.MAX_VALUE, player)
        player.health = 0.0
        if (player.isDead) return true
        for (i in 1..32) {
            player.damage(player.health, player)
            if (player.isDead) return true
        }
        return player.isDead
//        val source = DamageSource.builder(DamageTy).withCausingEntity(player).withDirectEntity(player).withDamageLocation().build()
//        val event = EntityDamageByEntityEvent(player, player, EntityDamageEvent.DamageCause.SUICIDE, )
    }
}