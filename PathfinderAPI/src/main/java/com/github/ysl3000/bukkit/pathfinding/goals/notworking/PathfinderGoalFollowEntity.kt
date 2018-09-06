package com.github.ysl3000.bukkit.pathfinding.goals.notworking

import org.bukkit.Material
import org.bukkit.entity.LivingEntity

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal

class PathfinderGoalFollowEntity(private val pathfinderGoalEntity: Insentient, private val entity: LivingEntity,
                                 private val moveRadius: Double, private val walkspeed: Double) : PathfinderGoal {

    override fun shouldExecute(): Boolean {
        return this.pathfinderGoalEntity.bukkitEntity.location
                .distanceSquared(entity.location) > moveRadius
    }

    /**
     * Whether the goal should Terminate
     *
     * @return true if should terminate
     */
    override fun shouldTerminate(): Boolean {
        return pathfinderGoalEntity.navigation.isDoneNavigating || this.entity.isDead
    }

    /**
     * Runs initially and should be used to setUp goalEnvironment Condition needs to be defined thus
     * your code in it isn't called
     */
    override fun init() {

    }

    /**
     * Is called when [.shouldExecute] returns true
     */
    override fun execute() {

        if (pathfinderGoalEntity.bukkitEntity.location
                        .add(pathfinderGoalEntity.bukkitEntity.location.direction.normalize())
                        .block.type != Material.AIR) {
            this.pathfinderGoalEntity.controllerJump.jump()
        }
    }

    override fun reset() {
        this.pathfinderGoalEntity.navigation.moveTo(this.entity, walkspeed)
    }

}