package io.github.keritial.keritize.spigot.command

import io.github.keritial.keritize.spigot.displayPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.logging.Logger

class SuicideCommand(private val logger: Logger, private val omit:Boolean) : CommandWrapper(requirePlayer = true), Listener {
    private val silentDeath = mutableListOf<Player>()
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val player = sender as Player
//        val source = DamageSource.builder(DamageTy).withCausingEntity(player).withDirectEntity(player).withDamageLocation().build()
//        val event = EntityDamageByEntityEvent(player, player, EntityDamageEvent.DamageCause.SUICIDE, )
        return killPlayer(player)
    }

    private fun killPlayer(player: Player): Boolean {
        silentDeath.add(player)
        player.health = 0.0
        if (player.isDead) {
            return true
        }
        logger.warning("For some unknown reason, failed to kill ${displayPlayer(player)}.")
        val tries = 712
        for (i in 1..tries) {
            if (player.isDead) {
                logger.warning("Killed ${displayPlayer(player)} after $i attempt(s).")
                return true
            }
            player.damage(player.health, player)
        }
        silentDeath.remove(player)
        logger.warning("Player ${displayPlayer(player)} is not killable, at least for $tries attempt(s).")
        return player.isDead
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (!omit) {
            return
        }
        val player = event.entity
        if (!silentDeath.contains((player))) {
            return
        }
//        player.lastDamageCause = EntityDamageEvent(player,EntityDamageEvent.DamageCause.SUICIDE,0.1)

        event.deathMessage = null
        logger.info("${player.name} (${player.uniqueId}) has suicided.")

        silentDeath.remove(player)
    }
}