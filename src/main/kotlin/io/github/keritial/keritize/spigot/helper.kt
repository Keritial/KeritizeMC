package io.github.keritial.keritize.spigot

import com.comphenix.protocol.ProtocolLibrary
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import java.nio.charset.StandardCharsets
import java.util.*

fun isPaper() = try {
    Class.forName("com.destroystokyo.paper.ParticleBuilder")
    true
} catch (e: ClassNotFoundException) {
    false
}

fun getManager() = try {
    ProtocolLibrary.getProtocolManager()
} catch (e: NoClassDefFoundError) {
    null
}

fun toGamemodeString(gamemode: GameMode) = when (gamemode) {
    GameMode.CREATIVE -> "creative"
    GameMode.SURVIVAL -> "survival"
    GameMode.ADVENTURE -> "adventure"
    GameMode.SPECTATOR -> "spectator"
}

fun generateOfflineUuid(characterName: String): UUID {
    return UUID.nameUUIDFromBytes(("OfflinePlayer:$characterName").toByteArray(StandardCharsets.UTF_8))
}

fun isPlayerLocationSafe(player: Player): Boolean {
    return isLocationSafe(player.location)
}

fun isLocationSafe(location: Location): Boolean {
//    val footBlock = location.block
////    val head2 = location.add(0.0, 1.0, 0.0).block
//    val headBlock = footBlock.getRelative(BlockFace.UP)
//    if ((footBlock.isSolid && headBlock.isSolid) || footBlock.isLiquid || headBlock.isLiquid) {
//        return false
//    }
//
//    val standBlock = footBlock.getRelative(BlockFace.DOWN)
//    if (!standBlock.isSolid) {
//        val material = standBlock.type
//        if (material.isAir || material == Material.LAVA) {
//            return false
//        }
//
//    }
    return true
}

fun findSafeLocation(location: Location): Location? {
    if (isLocationSafe(location)) {
        return location
    }
//    val top = location.world.getHighestBlockAt(location.blockX,location.blockZ).location
//    val top = location.
//    if (isLocationSafe(top)) {
//        return top
//    }
    return null
}

fun worldNameToHumanReadable(worldName: String) = when (worldName) {
    "overworld", "world" -> "主世界"
    "the_end", "world_the_end" -> "末地"
    "the_nether", "world_nether" -> "下界"
    else -> worldName
}

fun humanReadableToBoolean(input: String) = when (input) {
    "true", "1" -> true
    "false", "0" -> false
    else -> null
}

fun displayPlayer(player: Player) = "${player.name} (${player.uniqueId})"

fun displayCoordinates(location: Location) =
    "${toFixedXYZ(location.x, location.y, location.z)} (${location.world?.let { worldNameToHumanReadable(it.name) }})"

fun toFixedXYZ(x: Double, y: Double, z: Double) = "${toFixed(x, 2)} ${toFixed(y, 2)} ${toFixed(z, 2)}"

fun toFixed(number: Double, digits: Int) = "%.${digits}f".format(number)
