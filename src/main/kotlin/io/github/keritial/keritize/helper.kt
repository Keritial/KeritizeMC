package io.github.keritial.keritize

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace

fun isLocationSafe(location: Location): Boolean {
    val footBlock = location.block
//    val head2 = location.add(0.0, 1.0, 0.0).block
    val headBlock = footBlock.getRelative(BlockFace.UP)
    if ((footBlock.isSolid && headBlock.isSolid) || footBlock.isLiquid || headBlock.isLiquid) {
        return false
    }

    val standBlock = footBlock.getRelative(BlockFace.DOWN)
    if (!standBlock.isSolid) {
        val material = standBlock.type
        if (material.isAir || material == Material.LAVA) {
            return false
        }

    }
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

fun formatCoordinates(location: Location) {
    "${toFixedXYZ(location.x, location.y, location.z)}(${location.world.name})"
}

fun toFixedXYZ(x: Double, y: Double, z: Double) = "${toFixed(x, 1)} ${toFixed(y, 1)} ${toFixed(z, 1)}"

fun toFixed(number: Double, digits: Int) {
    "%.$digits".format(number)
}