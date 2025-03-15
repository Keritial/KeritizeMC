package io.github.keritial.keritize.spigot.feature

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.plugin.Plugin

fun registerRecipe(plugin: Plugin) {
    val rottenFleshToLeather = FurnaceRecipe(
        NamespacedKey(plugin, "rotten_flesh_to_leather"),
        ItemStack(Material.LEATHER),
        Material.ROTTEN_FLESH,
        0.1f,
        10 * 20
    )
    val rottenFleshToLeatherCampfire = CampfireRecipe(
        NamespacedKey(plugin, "rotten_flesh_to_leather_campfire"),
        rottenFleshToLeather.result,
        rottenFleshToLeather.inputChoice,
        rottenFleshToLeather.experience,
        rottenFleshToLeather.cookingTime * 2
    )
    val rottenFleshToLeatherSmoker = SmokingRecipe(
        NamespacedKey(plugin, "rotten_flesh_to_leather_smoker"),
        rottenFleshToLeather.result,
        rottenFleshToLeather.inputChoice,
        rottenFleshToLeather.experience,
        rottenFleshToLeather.cookingTime / 2
    )
    val bambooToSugarCane = ShapelessRecipe(
        NamespacedKey(plugin, "bamboo_to_sugar_cane"),
        ItemStack(Material.SUGAR_CANE, 3)
    ).addIngredient(ExactChoice(ItemStack(Material.BAMBOO, 4)))
    plugin.server.addRecipe(rottenFleshToLeather)
    plugin.server.addRecipe(rottenFleshToLeatherCampfire)
    plugin.server.addRecipe(rottenFleshToLeatherSmoker)
    plugin.server.addRecipe(bambooToSugarCane)
}