package com.github.ysl3000.bukkit.pathfinding.goals.notworking

import org.bukkit.entity.LivingEntity

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal

class PathfinderGoalFollowEntity(private val pathfinderGoalEntity: Insentient, private val entity: LivingEntity,
                                 private val moveRadius: Double, private val walkSpeed: Double) : PathfinderGoal {
    private var isAlreadySet: Boolean = false

    override fun shouldExecute(): Boolean {
        isAlreadySet = !this.pathfinderGoalEntity.navigation.isDoneNavigating
        return isAlreadySet
    }

    /**
     * Whether the goal should Terminate
     *
     * @return true if should terminate
     */
    override fun shouldTerminate(): Boolean {
        return if (!this.isAlreadySet) {
            true
        } else this.pathfinderGoalEntity.bukkitEntity.location
                .distance(this.entity.location) > this.moveRadius
    }

    /**
     * Runs initially and should be used to setUp goalEnvironment Condition needs to be defined thus
     * your code in it isn't called
     */
    override fun init() {
        if (!this.isAlreadySet) {
            this.pathfinderGoalEntity.navigation.moveTo(this.entity, walkSpeed)
        }
    }

    /**
     * Is called when [.shouldExecute] returns true
     */
    override fun execute() {
        this.pathfinderGoalEntity.controllerJump.jump()
    }

    override fun reset() {

    }
}