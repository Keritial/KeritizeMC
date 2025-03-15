package io.github.keritial.keritize.spigot

import io.github.keritial.keritize.channel
import io.github.keritial.keritize.spigot.command.tpa.TpacceptTabCompleter
import io.github.keritial.keritize.spigot.command.*
import io.github.keritial.keritize.spigot.command.back.BackCommand
import io.github.keritial.keritize.spigot.command.back.LastLocationService
import io.github.keritial.keritize.spigot.command.maintenance.MaintenanceCommand
import io.github.keritial.keritize.spigot.command.tpa.*
import io.github.keritial.keritize.spigot.feature.*
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.*
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import org.geysermc.geyser.api.GeyserApi
import java.time.Instant


class KeritizedSpigot : JavaPlugin(),PluginMessageListener {

//    val isPaper = isPaper()

//    private var lastTick: Long = 0
//    private var lastMSPT: Short = 0
//    val MSPT = ArrayList<Short>()


    override fun onPluginMessageReceived(p0: String, p1: Player, p2: ByteArray?) {
        if (p0 == channel.id) {

        }
    }

    override fun onEnable() {
        val start = Instant.now().toEpochMilli()

        saveDefaultConfig()


        load()

        logger.info("Finished loading in ${Instant.now().toEpochMilli() - start}ms.")
    }

    private fun load() {
        server.scheduler.runTaskAsynchronously(this, { _ ->
            server.scheduler.runTaskTimer(this, { _ ->
                val oldTime = System.currentTimeMillis()

                server.scheduler.runTaskLater(this, { _ ->
                    val newTime = System.currentTimeMillis()
                    millisecondsPerTick = (newTime - oldTime).toShort()
                }, 1)

            }, 0, 20)
        })

        registerCommand("keritize", KeritizeCommand(logger, this))

        if (config.getBoolean("prevent-chat-message-upgrades", true)) {
            logger.info("prevent-chat-message-upgrades enabled.")
            val prevent = PreventChatMessageUpgrades(logger, this)
            prevent.preventChatSessionUpdate()
            registerListener(prevent)
        }

        if (config.getBoolean("prevent-creeper-griefing", false)) {
            logger.info("prevent-creeper-griefing enabled.")
            registerListener(PreventCreeperGriefing(logger))
        }

        if (config.getBoolean("drop-player-skull-if-killed-by-player", false)) {
            logger.info("drop-player-skull-if-killed-by-player enabled.")
            registerListener(DropPlayerSkull(logger))
        }

//        val geyserAddon = registerGeyserAddon()
//
//        if (config.getBoolean("listen-proxy-command",false)) {
//            server.messenger.registerIncomingPluginChannel(this, channel.id, geyserAddon)
//        }

        registerCommand("migrateofflinepet", OfflinePetOwnerResetCommand(logger))

        registerCommand(
            "suicide",
            registerListener(SuicideCommand(logger, config.getBoolean("omit-suicide-message", true)))
        )

        registerCommand("hat", HatCommand())

        registerCommand("fly", FlyCommand())

        if (config.getBoolean("register-custom-recipe", false)) {
            logger.info("Registering custom recipes.")
            registerRecipe(this)
        }

        registerCommand("maintenance", registerListener(MaintenanceCommand(this,logger)))


        if (config.getBoolean("record-player-location", false)) {

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

        if (config.getBoolean("http-api", false)) {
            val port = config.getInt("http-port", 23333)

            val app = Indekkusu(port, logger, this)
//            val http = embeddedServer(Netty, port) {
//                routing {
//                    get("/") {
//                        call.respondText { "Hello world!" }
//                    }
//                }
//            }.start(wait = false)
            registerListener(app)
            indekkusu = app
        }

//        if (config.getBoolean("krtl-proprietary-beacon", false)) {
//            logger.info("Beacon enabled.")
//            val beacon = Beacon(
//                config.getString("krtl-proprietary-endpoint"),
//                plugin = this
//            )
//
////            server.scheduler.runTaskTimer(this, { task: BukkitTask ->
////                if (Instant.now().epochSecond - beacon.lastSentBeacon < 5) {
////                    return@runTaskTimer
////                }
////                beacon.sendBeacon()
////            }, 0, 15 * 20)
//            server.scheduler.runTaskTimer(this, { task: BukkitTask ->
//                val epochMilliseconds = Instant.now().toEpochMilli()
//                lastMSPT = (epochMilliseconds - lastTick).toShort()
//                lastTick = epochMilliseconds
//                MSPT.removeAt(15)
//                MSPT.add(0, lastMSPT)
////            MSPT[(epochMilliseconds%10).toInt()] = lastMSPT.toInt()
//
//            }, 0, 1)
//
//            registerListener(beacon)
//        }
    }

    override fun onDisable() {
        logger.info("Shutting down...")
        unload()
    }

    private var indekkusu: Indekkusu? = null

    var millisecondsPerTick = (0).toShort()

    private fun unload() {
        indekkusu?.app?.stop()
    }

    private fun <T: CommandExecutor> registerCommand(
        commandLiteral: String,
        executor: T,
        completer: TabCompleter? = null
    ): PluginCommand? {
        val command = getCommand(commandLiteral) ?: return null
        command.setExecutor(executor)
        if (completer == null) {
            if (executor is TabCompleter) {
                command.tabCompleter = executor
            }
//            command.tabCompleter = BlankTabCompleter()
        } else {
            command.tabCompleter = completer
        }
        return command
    }

    private fun <T : Listener> registerListener(listener: T): T {
        server.pluginManager.registerEvents(listener, this)
        return listener
    }

//    private fun registerGeyserAddon(): GeyserAddon {
//        val geyserApi = try {
//            GeyserApi.api()
//        } catch (e: NoClassDefFoundError) {
////            logger.info("No Geyser installed. Skipping Geyser add-on registration.")
//            null
//        }
//        logger.info("Geyser Add-on registered.")
//        return registerListener(GeyserAddon(geyserApi, logger, channel.id))
//    }


//    private fun printLogo() {
//        println()
//        println("=======================================================")
//        println("██╗  ██╗███████╗██████╗ ██╗████████╗██╗███████╗███████╗")
//        println("██║ ██╔╝██╔════╝██╔══██╗██║╚══██╔══╝██║╚══███╔╝██╔════╝")
//        println("█████╔╝ █████╗  ██████╔╝██║   ██║   ██║  ███╔╝ █████╗  ")
//        println("██╔═██╗ ██╔══╝  ██╔══██╗██║   ██║   ██║ ███╔╝  ██╔══╝  ")
//        println("██║  ██╗███████╗██║  ██║██║   ██║   ██║███████╗███████╗")
//        println("╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝   ╚═╝   ╚═╝╚══════╝╚══════╝")
//        println("=======================================================")
//        println()
//    }

    //    private fun preventChatSessionUpgrades() {
//        try {
////            val packetClass = Class.forName("net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket")
////            val handleMethod = packetClass.getDeclaredMethod(
////                "handle",
////                Class.forName("net.minecraft.network.protocol.game.ServerGamePacketListener")
////            )
////            handleMethod.isAccessible = true
////
////            val noopHandleMethod: (Any, Array<Any>) -> Unit = { _, _ -> }
////
////            val proxyHandleMethod = Proxy.newProxyInstance(
////                handleMethod.declaringClass.classLoader,
////                arrayOf(Method::class.java),
////                { _, method, _ ->
////                    if (method.name == handleMethod.name) {
////                        noopHandleMethod
////                    } else {
////                        method.invoke(handleMethod)
////                    }
////                }
////            ) as Method
////
////            val field = handleMethod.declaringClass.getDeclaredField("handle")
////            field.isAccessible = true
////            field.set(null, proxyHandleMethod)
//            val packetClass = Class.forName("net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket", true, this::class.java.classLoader)
//            val listenerClass = Class.forName("net.minecraft.network.protocol.PacketListener", true, this::class.java.classLoader)
//            val handleMethod = packetClass.getDeclaredMethod("handle", listenerClass)
//            handleMethod.isAccessible = true
//
//            val noopHandleMethod: (Any, Array<Any>) -> Unit = { _, _ -> }
//
//            val proxyHandleMethod = Proxy.newProxyInstance(
//                handleMethod.declaringClass.classLoader,
//                arrayOf(Method::class.java),
//                { _, method, _ ->
//                    if (method.name == handleMethod.name) {
//                        noopHandleMethod
//                    } else {
//                        method.invoke(handleMethod)
//                    }
//                }
//            ) as Method
//
//            val field = handleMethod.declaringClass.getDeclaredField("handle")
//            field.isAccessible = true
//            field.set(null, proxyHandleMethod)
//            logger.info("Successfully hooked method.")
//        } catch (e: Exception) {
//            logger.warning("Failed to hook method.")
//            e.printStackTrace()
//        }
//    }

    //    private fun isProtocolLibInstalled(): Boolean {
//        return try {
//            // Try to load a class from ProtocolLib to check if it's installed
//            Class.forName("com.comphenix.protocol.ProtocolLibrary")
//            true
//        } catch (e: ClassNotFoundException) {
//            false
//        }
//    }
}

