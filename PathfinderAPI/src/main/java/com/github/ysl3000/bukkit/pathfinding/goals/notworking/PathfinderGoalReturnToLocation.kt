package com.github.ysl3000.bukkit.pathfinding.goals.notworking

import org.bukkit.Location
import org.bukkit.entity.LivingEntity

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient
import com.github.ysl3000.bukkit.pathfinding.pathfinding.Navigation
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal

/**
 * Created by Yannick on 30.11.2016.
 */
class PathfinderGoalReturnToLocation(private val pathfinderGoalEntity: Insentient, private val entity: LivingEntity,
                                     private val moveRadius: Double) : PathfinderGoal {

    private val navigation: Navigation
    private val loc: Location

    init {
        this.loc = entity.location
        this.navigation = pathfinderGoalEntity.navigation
    }


    override fun shouldExecute(): Boolean {
        return this.loc.distance(this.entity.location) > this.moveRadius
    }

    override fun shouldTerminate(): Boolean {
        return !navigation.isDoneNavigating
    }

    override fun init() {
        if (this.navigation.isDoneNavigating) {
            this.navigation.moveTo(this.loc, 1.0)
        }
    }

    override fun execute() {
        this.pathfinderGoalEntity.controllerJump.jump()
    }


    override fun reset() {

    }
}
