package io.github.keritial.keritize

import org.bukkit.Location
import org.bukkit.entity.Player

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
    val top = location.toHighestLocation()
    if (isLocationSafe(top)) {
        return top
    }
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

fun formatCoordinates(location: Location) =
    "${toFixedXYZ(location.x, location.y, location.z)} (${worldNameToHumanReadable(location.world.name)})"

fun toFixedXYZ(x: Double, y: Double, z: Double) = "${toFixed(x, 2)} ${toFixed(y, 2)} ${toFixed(z, 2)}"

fun toFixed(number: Double, digits: Int) = "%.${digits}f".format(number)
