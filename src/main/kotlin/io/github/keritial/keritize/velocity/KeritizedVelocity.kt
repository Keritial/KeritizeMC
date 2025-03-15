package io.github.keritial.keritize.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.geysermc.event.subscribe.Subscribe
import org.geysermc.geyser.api.GeyserApi
import org.slf4j.Logger


@Plugin(
    id = "keritize",
    name = "Keritized Velocity",
    version = "0.1.0-SNAPSHOT",
    url = "https://krtl.top",
    description = "Velocity plugin",
    authors = ["Keritial"]
)
class KeritizedVelocity @Inject constructor(private val server: ProxyServer, private val logger: Logger) {

    private val geyserApi:GeyserApi? by lazy {
        try {
            GeyserApi.api()
        } catch (e: NoClassDefFoundError) {
            null
        }
    }

    @Subscribe
    fun onInitialize(event: ProxyInitializeEvent) {
        logger.info("Running in Velocity.")
    }
    @Subscribe
    fun onPlayerChat(event: PlayerChatEvent) {
        if (geyserApi==null) {
            return
        }
        if (!geyserApi!!.isBedrockPlayer(event.player.uniqueId)) {
            return
        }
        val message = event.message
        if (!isChatPrefixed(message)) {
            return
        }
        val content = message.substring(1)
        if (isChatPrefixed(content)) {
            event.result = PlayerChatEvent.ChatResult.message(content)
        } else {
            event.result = PlayerChatEvent.ChatResult.message("/$content")
        }

    }

    private fun isChatPrefixed(message:String): Boolean {
        return message.startsWith("!") && message.startsWith("！")
    }
}