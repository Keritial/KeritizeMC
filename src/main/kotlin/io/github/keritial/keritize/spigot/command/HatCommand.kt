package io.github.keritial.keritize.spigot.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class HatCommand : CommandWrapper(requirePlayer = true) {
    override fun execute(
        sender: CommandSender,
        command: Command,
        commandLiteral: String,
        strings: Array<out String>
    ): Boolean {
        val player = sender as Player
        val inventory = player.inventory
        val hand = inventory.itemInMainHand

        if (hand.type.isAir) {
            player.sendMessage("请在手中抓点什么。")
            return true
        }
        if (hand.type.maxDurability != (0).toShort()) {
            player.sendMessage("不支持的物品。")
            return true
        }
        val head = inventory.helmet

        if (head != null && head.enchantments.containsKey(Enchantment.BINDING_CURSE)) {
            player.sendMessage("被绑定诅咒阻拦了。")
            return true
        }

        inventory.helmet = hand
        inventory.setItemInMainHand(head)
        return true
    }
}