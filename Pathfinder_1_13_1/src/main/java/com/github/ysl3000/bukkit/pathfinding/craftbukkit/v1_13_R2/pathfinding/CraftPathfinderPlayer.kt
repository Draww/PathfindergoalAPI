package com.github.ysl3000.bukkit.pathfinding.craftbukkit.v1_13_R2.pathfinding

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer
import org.bukkit.entity.Player

import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderPlayer

import net.minecraft.server.v1_13_R2.EntityPlayer

/**
 * Created by ysl3000
 */
class CraftPathfinderPlayer private constructor(private val entityPlayer: EntityPlayer) : PathfinderPlayer {

    val STILL = -0.0784000015258789

    override val player: Player
        get() = entityPlayer.bukkitEntity

    override val relativeMotionX: Double
        get() = entityPlayer.motX

    override val relativeMotionY: Double
        get() = entityPlayer.motY

    override val relativeMotionZ: Double
        get() = entityPlayer.motZ

    override val relativeMotionYaw: Float
        get() = entityPlayer.yaw

    override val relativeMotionPitch: Float
        get() = entityPlayer.pitch

    override val motionForward: Float
        get() = entityPlayer.bh

    override val motionSideward: Float
        get() = entityPlayer.bj

    override val isJumping: Boolean
        get() = entityPlayer.motY > STILL

    constructor(player: Player) : this((player as CraftPlayer).handle) {}


}
