package io.github.keritial.keritize.command

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

        if (hand.isEmpty) {
            player.sendMessage("请在手中抓点什么。")
        }
        if (hand.type.maxDurability != (0).toShort()) {
            player.sendMessage("不支持的物品。")
        }
        // v1_9_4_R01
        val head = inventory.helmet

        if (head != null && head.enchantments.containsKey(Enchantment.BINDING_CURSE)) {
            player.sendMessage("被绑定诅咒阻拦了，请先解决它。")
        }

        inventory.helmet = hand
        inventory.setItemInMainHand(head)
        return true
    }
}