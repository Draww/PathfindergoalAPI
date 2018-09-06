package com.github.ysl3000.bukkit.pathfinding.pathfinding

import org.bukkit.entity.Player

/**
 * The PathfinderPlayer grants access to functionality needed for PathfinderEntities e.g. get the
 * relative Motion of the player
 */
interface PathfinderPlayer {

    /**
     * Returns the Bukkit Player
     *
     * @return Bukkit Player
     */
    val player: Player

    val relativeMotionX: Double

    val relativeMotionY: Double

    val relativeMotionZ: Double

    val relativeMotionYaw: Float

    val relativeMotionPitch: Float

    val motionForward: Float

    val motionSideward: Float

    val isJumping: Boolean

}