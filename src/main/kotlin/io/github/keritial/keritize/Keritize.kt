package io.github.keritial.keritize

import io.github.keritial.keritize.command.*
import io.github.keritial.keritize.command.back.*
import io.github.keritial.keritize.command.maintenance.MaintenanceCommand
import io.github.keritial.keritize.command.tpa.*
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener
import org.bukkit.inventory.*
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.geyser.api.GeyserApi
import java.time.Instant

@Suppress("unused")
class Keritize : JavaPlugin() {

    override fun onEnable() {
        val start = Instant.now().toEpochMilli()

        printLogo()

        prepare()

        logger.info("Finished loading. Time taken: ${Instant.now().toEpochMilli() - start}milliseconds")
    }

    override fun onDisable() {
        logger.info("Shutting down... doing nothing actually.")
    }

    private fun prepare() {

        registerGeyserAddon()

        registerRecipe()

        registerListener(EventTaker(logger))

        registerCommand("suicide", SuicideCommand())
        registerCommand("hat", HatCommand())
        registerCommand("fly", FlyCommand())

        val maintenanceCommand = MaintenanceCommand()
        registerCommand("maintenance", maintenanceCommand)
        registerListener(maintenanceCommand)

        val lastLocationService = LastLocationService(logger)
        val tpaService = TpaService(this)
        registerListener(lastLocationService)
        registerListener(tpaService)

        registerCommand("back", BackCommand(lastLocationService))

        registerCommand("tpa", TpaCommand(tpaService), TpaTabCompleter())
        registerCommand("tpahere", TpaCommand(tpaService, true), TpaTabCompleter())
        registerCommand("tpaccept", TpacceptCommand(tpaService), TpacceptTabCompleter(tpaService))
        registerCommand("tpacancel", TpacancelCommand(tpaService))
        registerCommand("tpadeny", TpadenyCommand(tpaService), TpacceptTabCompleter(tpaService))

    }

    private fun registerRecipe() {
        val rottenFleshToLeather = FurnaceRecipe(
            NamespacedKey(this, "rotten_flesh_to_leather"),
            ItemStack(Material.LEATHER),
            Material.ROTTEN_FLESH,
            0.1f,
            10 * 20
        )
        val rottenFleshToLeatherCampfire = CampfireRecipe(
            NamespacedKey(this, "rotten_flesh_to_leather_campfire"),
            rottenFleshToLeather.result,
            rottenFleshToLeather.inputChoice,
            rottenFleshToLeather.experience,
            rottenFleshToLeather.cookingTime * 2
        )
        val rottenFleshToLeatherSmoker = SmokingRecipe(
            NamespacedKey(this, "rotten_flesh_to_leather_smoker"),
            rottenFleshToLeather.result,
            rottenFleshToLeather.inputChoice,
            rottenFleshToLeather.experience,
            rottenFleshToLeather.cookingTime / 2
        )
        val bambooToSugarCane = ShapelessRecipe(
            NamespacedKey(this, "bamboo_to_sugar_cane"),
            ItemStack(Material.SUGAR_CANE, 3)
        ).addIngredient(ItemStack(Material.BAMBOO, 4))
        server.addRecipe(rottenFleshToLeather)
        server.addRecipe(rottenFleshToLeatherCampfire)
        server.addRecipe(rottenFleshToLeatherSmoker)
        server.addRecipe(bambooToSugarCane)
    }

    private fun registerCommand(commandLiteral: String, executer: CommandExecutor, completer: TabCompleter? = null): PluginCommand? {
        val command = getCommand(commandLiteral) ?: return null
        command.setExecutor(executer)
        if (completer == null) {
            command.tabCompleter = BlankTabCompleter()
        } else {
            command.tabCompleter = completer
        }
        return command
    }

    private fun registerListener(listener: Listener): Listener {
        server.pluginManager.registerEvents(listener, this)
        return listener
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

    private fun printLogo() {
        println()
        println("=======================================================")
        println("██╗  ██╗███████╗██████╗ ██╗████████╗██╗███████╗███████╗")
        println("██║ ██╔╝██╔════╝██╔══██╗██║╚══██╔══╝██║╚══███╔╝██╔════╝")
        println("█████╔╝ █████╗  ██████╔╝██║   ██║   ██║  ███╔╝ █████╗  ")
        println("██╔═██╗ ██╔══╝  ██╔══██╗██║   ██║   ██║ ███╔╝  ██╔══╝  ")
        println("██║  ██╗███████╗██║  ██║██║   ██║   ██║███████╗███████╗")
        println("╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝   ╚═╝   ╚═╝╚══════╝╚══════╝")
        println("=======================================================")
        println()
    }
}
