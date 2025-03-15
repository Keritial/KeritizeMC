package io.github.keritial.keritize.spigot.command.tpa

//import io.papermc.paper.util.Tick
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

//import org.bukkit.scheduler.BukkitTask
//import java.time.Duration

const val TPA_TIMEOUT = 120

class TpaService(private val plugin: JavaPlugin) : Listener {

    private val requests = HashSet<TpaRequest>()
    private val tasks = HashMap<TpaRequest, Runnable>()

    fun request(requester: Player, acceptor: Player, reversed: Boolean) {
        add(requester, acceptor, reversed)

        requester.sendMessage(
            if (reversed) {
                "已请求 ${acceptor.name} 传送到你处，${TPA_TIMEOUT} 秒后过期。"
            } else {
                "已请求传送到 ${acceptor.name}，${TPA_TIMEOUT} 秒后过期。"
            }
        )
        acceptor.sendMessage(
            if (reversed) {
                "${requester.name} 请求你传送到对方处，${TPA_TIMEOUT} 秒后过期。"
            } else {
                "${requester.name} 请求传送到你处，${TPA_TIMEOUT} 秒后过期。"
            }
        )
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        removePlayer(event.player)
    }

    fun byRequester(requester: Player) = this.requests.find { it.requester == requester }

    fun byAcceptor(acceptor: Player) = this.requests.filter { it.acceptor == acceptor }

    fun add(requester: Player, acceptor: Player, reversed: Boolean = false, timeout: Long = TPA_TIMEOUT.toLong()) {
        val existingRequest = byRequester(requester)

        val request = TpaRequest(requester, acceptor, reversed)
        if (request == existingRequest) {
            requester.sendMessage("你已经发送过同样的请求了。")
            return
        }
        if (existingRequest != null) {
            remove(existingRequest)
        }

//        val runnable = Runnable {
//            remove(request)
//        }

//        plugin.server.scheduler.runTaskLaterAsynchronously(
//            plugin,
//            ,
//            Tick.tick().fromDuration(Duration.ofSeconds(20.toLong())).toLong()
//        )

        requests.add(request)
//        tasks[request] = runnable
    }

    fun remove(request: TpaRequest) {
        requests.remove(request)
//        tasks[request]?.
        tasks.remove(request)
    }

    fun removePlayer(player: Player) {
        deny(player)
        cancel(player)
    }

    fun cancel(requester: Player): Boolean {
        val request = byRequester(requester) ?: return false
        remove(request)
        return true
    }

    fun deny(requester: Player): Boolean {
        val requests = byAcceptor(requester)
        if (requests.isEmpty()) {
            return false
        }
        requests.forEach { remove(it) }
        return true
    }
}

data class TpaRequest(
    val requester: Player,
    val acceptor: Player,
    val reversed: Boolean = false,
    var confirmed: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TpaRequest

        if (requester != other.requester) return false
        if (acceptor != other.acceptor) return false
        if (reversed != other.reversed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = requester.hashCode()
        result = 31 * result + acceptor.hashCode()
        result = 31 * result + reversed.hashCode()
        return result
    }
}