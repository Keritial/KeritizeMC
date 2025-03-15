package io.github.keritial.keritize.spigot.useless

import io.github.keritial.keritize.spigot.KeritizedSpigot
import org.bukkit.event.Listener

class Beacon(private val endpoint:String?,private val plugin: KeritizedSpigot) :Listener{
//
//    @EventHandler
//    fun onJoin(event: PlayerJoinEvent) {
//        send(
//            "event",
//            Json.encodeToString(EventRef("PlayerJoinEvent", toPlayerRef(event.player), Instant.now().toEpochMilli()))
//        )
//    }
//
//    @EventHandler
//    fun onLeave(event: PlayerQuitEvent) {
//        send(
//            "event",
//            Json.encodeToString(EventRef("PlayerQuitEvent", toPlayerRef(event.player), Instant.now().toEpochMilli()))
//        )
//    }
//
//    @EventHandler
//    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
//        send("command", Json.encodeToString(toChatRef(toPlayerRef(event.player), event.message)))
//    }
//
//    @EventHandler
//    fun onServerCommand(event: ServerCommandEvent) {
//        send("command", Json.encodeToString(toChatRef(null, event.command)))
//    }
//
//    @EventHandler
//    fun onChat(event: AsyncPlayerChatEvent) {
//        send("chat", Json.encodeToString(toChatRef(toPlayerRef(event.player), event.message)))
//    }
//
//
//
//    var lastSentBeacon: Long = 0
//
    fun sendBeacon() {
//        lastSentBeacon = Instant.now().epochSecond
//        send(
//            "beacon",
//            Json.encodeToString(
//                toBeacon(
//                    plugin.server,
//                    (plugin.MSPT.reduce { a, b -> (a + b).toShort() } / plugin.MSPT.size.toShort()).toShort()
//                )
//            )
//        )
    }
//
//    private fun send(path: String, body: String) {
//        plugin.server.scheduler.runTaskAsynchronously(plugin, { task: BukkitTask ->
//
//            val client = HttpClient.newBuilder()
//                .version(HttpClient.Version.HTTP_2)
//                .followRedirects(HttpClient.Redirect.NORMAL)
//                .connectTimeout(Duration.ofSeconds(16))
//                .build()
//
//            val request = HttpRequest.newBuilder()
//                .uri(URI.create("$endpoint/$path"))
//                .POST(HttpRequest.BodyPublishers.ofString(body))
//                .build()
//
//            client.send(request, HttpResponse.BodyHandlers.ofString())
//        })
//    }
}
//
//
//
//@Serializable
//data class ReportBeacon(val players: List<PlayerInfo>, val timestamp: Long, val MSPT: Short)
//
//@Serializable
//data class PlayerRef(
//    val name: String,
//    val uuid: String
//)
//
//@Serializable
//data class PlayerInfo(
//    val info: PlayerRef,
//    val health: Double,
//    val gamemode: String,
//    val ping: Int,
//    val locate: String,
//    val address: String
//)
//
//@Serializable
//data class ChatRef(
//    val sender: PlayerRef?,
//    val content: String,
//    val timestamp: Long
//)
//
//@Serializable
//data class EventRef(
//    val name: String,
//    val player: PlayerRef?,
//    val timestamp: Long
//)
//
//fun toBeacon(server: Server, MSPT: Short) = ReportBeacon(
//    server.onlinePlayers.map { toPlayerInfo(it) },
//    Instant.now().toEpochMilli(),
//    MSPT
//)
//
//fun toPlayerRef(player: Player) = PlayerRef(
//    player.name,
//    player.uniqueId.toString()
//)
//
//fun toPlayerInfo(player: Player) = PlayerInfo(
//    toPlayerRef(player),
//    player.health,
//    toGamemodeString(player.gameMode),
//    player.ping,
//    player.locale,
//    player.address.toString()
//)
//
//fun toChatRef(p: PlayerRef?, content: String) = ChatRef(p, content, Instant.now().toEpochMilli())