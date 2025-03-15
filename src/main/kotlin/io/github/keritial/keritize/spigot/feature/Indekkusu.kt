package io.github.keritial.keritize.spigot.feature

import io.github.keritial.keritize.spigot.KeritizedSpigot
import io.github.keritial.keritize.spigot.toGamemodeString
import io.javalin.Javalin
import io.javalin.http.util.NaiveRateLimit
import io.javalin.util.JavalinLogger
import io.javalin.websocket.WsContext
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerCommandEvent
import java.time.Instant
import java.util.concurrent.TimeUnit
import java.util.logging.Logger


class Indekkusu(port: Int, private val logger: Logger, private val plugin: KeritizedSpigot) : Listener {
    val app: Javalin

    private val chatHistory = mutableListOf<ChatMessage>()
    private val commandInvocation = mutableListOf<ChatMessage>()
    private val entryAndExit = mutableListOf<SimplePlayerEventRef>()

    private val websocketSession = mutableListOf<WsContext>()

    init {
        logger.warning("HTTP API is not finished!!! Port:$port")

        JavalinLogger.startupInfo = false

        app = Javalin.create()

        app.apply {
            before { ctx ->
                NaiveRateLimit.requestPerTimeUnit(ctx, 2, TimeUnit.SECONDS)

                val origin = ctx.header("Origin")
                val referer = ctx.header("Referer")
                logger.info("[API] ${ctx.path()}, origin: $origin, referer: $referer")
//                if (origin != null) {
//                    try {
//                        val uri = URI(origin)
//                        val permit = "krtl.top"
//                        val host = uri.host
//                        if (permit == host || host.endsWith(".$permit")) {
//                            ctx.header("Access-Control-Allow-Origin", origin)
//                        }
//                    } catch (_: Exception) {
//                    }
//                }
                ctx.header("Server", "KRTL")
            }

            wsBefore { ws ->
                // runs before all WebSocket requests
            }

            ws("/ws") { ws ->
                ws.onConnect { ctx ->
                    websocketSession.add(ctx)
                }
                ws.onClose { ctx ->
                    websocketSession.remove(ctx)
                }

            }

            get("/") { ctx ->

                ctx.json(
                    ServerOverview(
                        plugin.server.onlinePlayers.map { it.toInfo().copy(address = null) }
                            .toList(),
                        chatHistory,
                        entryAndExit,
                        plugin.millisecondsPerTick,
                        Instant.now().toEpochMilli()
                    )
                )

            }
            get("/player") { ctx ->
                ctx.json(plugin.server.onlinePlayers.map { it.toInfo().copy(address = null) })

            }
            get("/entryAndExit") { ctx ->
                ctx.json(entryAndExit)
            }
            get("/chat") { ctx ->
                ctx.json(chatHistory)
            }
            get("/commandInvocations") { ctx ->
                ctx.json(commandInvocation)
            }
        }

        app.start(port)
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        chatHistory.add(event.toRef())
        scheduleDelete(chatHistory)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        entryAndExit.add(event.toRef())
        scheduleDelete(entryAndExit)
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        entryAndExit.add(event.toRef())
        scheduleDelete(entryAndExit)
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        commandInvocation.add(event.toRef())
        scheduleDelete(commandInvocation)
    }

    @EventHandler
    fun onServerCommand(event: ServerCommandEvent) {
        commandInvocation.add(event.toRef())
        scheduleDelete(commandInvocation)
    }

    private fun <T> scheduleDelete(list: MutableList<T>) {
        plugin.server.scheduler.runTaskLater(plugin, { _ -> list.removeFirst() }, 1 * 60 * 60 * 20)
    }

}

data class SimplePlayerEventRef(
    val player: PlayerRef,
    val eventName: String,
    val timestamp: Long
)

data class ServerOverview(
    val player: List<PlayerInfo>,
    val chatHistory: List<ChatMessage>,
    val entryAndExit: List<SimplePlayerEventRef>,
    val mspt: Short,
    val timestamp: Long
)

fun PlayerEvent.toRef() = SimplePlayerEventRef(player.toRef(), eventName, Instant.now().toEpochMilli())

data class ChatMessage(
    val sender: PlayerRef?,
    val content: String,
    val timestamp: Long
)

fun AsyncPlayerChatEvent.toRef() = ChatMessage(player.toRef(), message, Instant.now().toEpochMilli())

fun PlayerCommandPreprocessEvent.toRef() = ChatMessage(player.toRef(), message, Instant.now().toEpochMilli())

fun ServerCommandEvent.toRef() = ChatMessage(null, command, Instant.now().toEpochMilli())

data class PlayerRef(
    val uuid: String,
    val name: String,
)

data class PlayerInfo(
    val id: PlayerRef,
    val gamemode: String?,
    val health: Double?,
    val ping: Short?,
    val locate: String?,
    val address: String?
)

fun Player.toInfo() = PlayerInfo(
    toRef(),
    toGamemodeString(gameMode),
    health,
    ping.toShort(),
    locale,
    address.toString()
)

fun Player.toRef() = PlayerRef(
    uniqueId.toString(),
    name,
)