package com.github.ysl3000.bukkit.pathfinding.goals

import org.bukkit.Location
import org.bukkit.Material

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient
import com.github.ysl3000.bukkit.pathfinding.pathfinding.Navigation
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal

class PathfinderGoalMoveToLocation(private val pathfinderGoalEntity: Insentient, private val targetLocation: Location,
                                   private val walkSpeed: Double, private val distance: Double) : PathfinderGoal {
    private val navigation: Navigation

    private var isAlreadySet = false
    private val isDone = false


    init {
        this.navigation = pathfinderGoalEntity.navigation
    }

    override fun shouldExecute(): Boolean {
        return if (this.isAlreadySet) {
            false
        } else pathfinderGoalEntity.bukkitEntity.location.distanceSquared(targetLocation) > distance
    }


    override fun shouldTerminate(): Boolean {
        return this.isAlreadySet = !this.pathfinderGoalEntity.navigation.isDoneNavigating
    }

    override fun init() {
        if (!this.isAlreadySet) {
            this.navigation.moveTo(this.targetLocation, walkSpeed)
        }
    }

    override fun execute() {
        if (pathfinderGoalEntity.bukkitEntity.location.add(pathfinderGoalEntity.bukkitEntity.location.direction.normalize())
                        .block.type != Material.AIR) {
            pathfinderGoalEntity.controllerJump.jump()
        }

    }

    override fun reset() {}


    private fun setMessage(message: String) {
        pathfinderGoalEntity.bukkitEntity.customName = message
        pathfinderGoalEntity.bukkitEntity.isCustomNameVisible = true
    }

}