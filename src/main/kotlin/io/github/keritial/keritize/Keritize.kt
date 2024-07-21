package io.github.keritial.keritize

import io.github.keritial.keritize.command.*
import io.github.keritial.keritize.command.back.BackCommand
import io.github.keritial.keritize.command.back.LastLocationService
import io.github.keritial.keritize.command.tpa.*
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.geyser.api.GeyserApi

@Suppress("unused")
class Keritize : JavaPlugin() {

    override fun onEnable() {
        printName()

        registerGeyserAddon()

        registerListener(EventTaker())

        registerCommand("suicide", SuicideCommand())

        val lastLocationService = LastLocationService(logger)
        registerCommand("back", BackCommand(lastLocationService))
        registerListener(LocationListener(logger, lastLocationService))

        val tpaService = TpaService(this)
        registerCommand("tpa", TpaCommand(tpaService))
        registerCommand("tpahere", TpaCommand(tpaService, true))
        registerCommand("tpaccept", TpacceptCommand(tpaService))
        registerCommand("tpacancel", TpacancelCommand(tpaService))
        registerCommand("tpadeny", TpadenyCommand(tpaService))

        logger.info("Loaded.")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun registerCommand(commandLiteral: String, executer: CommandExecutor, completer: TabCompleter? = null) {
        val command = getCommand(commandLiteral) ?: return
        command.setExecutor(executer)
        if (completer != null) {
            command.tabCompleter = completer
        }
    }

    private fun registerListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    private fun getGeyserApi(): GeyserApi? =
        try {
            GeyserApi.api()
        } catch (e: NoClassDefFoundError) {
            null
        }

    private fun registerGeyserAddon() {
        val geyserApi = getGeyserApi()
        if (geyserApi != null) {
            registerListener(GeyserAddon(geyserApi, logger))
            logger.info("Geyser Add-on registered.")
        } else {
            logger.info("No Geyser installed. Skipping Geyser Add-on registration.")
        }
    }
    private fun printName() {
        println()
        println("██╗  ██╗███████╗██████╗ ██╗████████╗██╗███████╗███████╗")
        println("██║ ██╔╝██╔════╝██╔══██╗██║╚══██╔══╝██║╚══███╔╝██╔════╝")
        println("█████╔╝ █████╗  ██████╔╝██║   ██║   ██║  ███╔╝ █████╗  ")
        println("██╔═██╗ ██╔══╝  ██╔══██╗██║   ██║   ██║ ███╔╝  ██╔══╝  ")
        println("██║  ██╗███████╗██║  ██║██║   ██║   ██║███████╗███████╗")
        println("╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝   ╚═╝   ╚═╝╚══════╝╚══════╝")
        println()
    }
}
