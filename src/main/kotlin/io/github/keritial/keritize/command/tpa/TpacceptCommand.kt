package io.github.keritial.keritize.command.tpa

import io.github.keritial.keritize.command.CommandWrapper
import io.github.keritial.keritize.findSafeLocation
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.logging.Logger

class TpacceptCommand(private val tpaService: TpaService) :
    CommandWrapper(requirePlayer = true) {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val player = sender as Player
        val requests = tpaService.byAcceptor(player)
//        val confirm = when (val size = strings.size) {
//            0 -> false
//            else -> when (size) {
//                1 -> strings[0]
//                else -> strings[1]
//            } == "confirm"
//        }
        when (requests.size) {
            0 -> {
                player.sendMessage("无传送请求。")
                return true
            }

            1 -> {
                val request = requests[0]
                execute(request)
                return true
            }
        }
        val players = requests.joinToString(", ") { it.requester.name }
        if (strings.isEmpty()) {
            player.sendMessage("有多个传送请求，请从 $players 中选择一个。")
            return true
        }
        val id = strings[0]
        val requester = Bukkit.getPlayer(id)

        if (requester == null) {
            player.sendMessage("$id 不存在，请从 $players 中选择一个。")
            return true
        }
        val request = requests.first { it.requester == requester }
        execute(request)
        return true
    }

    fun execute(request: TpaRequest) {
        val reversed = request.reversed
        val from = if (reversed) {
            request.acceptor
        } else {
            request.requester
        }
        val to = if (reversed) {
            request.requester
        } else {
            request.acceptor
        }
        val originalLocation = to.location
        val safeLocation = findSafeLocation(originalLocation)
        val destination = safeLocation ?: originalLocation
        if (safeLocation == null && !request.confirmed) {
            val doublecheckmessage = "对方位置不安全。请再次执行 /tpaccept 指令以确认。"
            if (reversed) {
                request.confirmed = true
                request.acceptor.sendMessage(doublecheckmessage)
            } else {
                request.acceptor.sendMessage("你的位置不安全。待对方再次批准此次请求，${TPA_TIMEOUT} 秒后过期。")
                request.requester.sendMessage(doublecheckmessage)
                tpaService.add(request.acceptor, request.requester, true)
                tpaService.remove(request)
            }
            return
        }
        request.acceptor.sendMessage("批准传送请求。")

        from.teleport(
            destination
        )
        tpaService.remove(request)
    }

}