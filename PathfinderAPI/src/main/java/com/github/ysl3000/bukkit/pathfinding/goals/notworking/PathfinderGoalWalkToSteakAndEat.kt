package com.github.ysl3000.bukkit.pathfinding.goals.notworking

import java.util.Comparator
import java.util.Optional

import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal

/**
 * Created by ysl3000 on 09.12.16. todo check if working else update
 */
class PathfinderGoalWalkToSteakAndEat(private val pathfinderGoalEntity: Insentient, private val distance: Double) : PathfinderGoal {

    private var target: Entity? = null

    private val distanceComperator: DistanceComperator

    init {
        this.distanceComperator = DistanceComperator()
    }


    override fun shouldExecute(): Boolean {
        return target != null && !target!!.isDead && distanceComperator.getDistance(target!!) > 0
    }

    /**
     * Whether the goal should Terminate
     *
     * @return true if should terminate
     */
    override fun shouldTerminate(): Boolean {
        return target == null || target!!.isDead
    }

    /**
     * Runs initially and should be used to setUp goalEnvironment Condition needs to be defined thus
     * your code in it isn't called
     */
    override fun init() {
        val nearestStack = pathfinderGoalEntity.bukkitEntity
                .getNearbyEntities(distance, distance, distance).stream()
                .filter { e -> e.type == EntityType.DROPPED_ITEM }.min(distanceComperator)
        nearestStack.ifPresent { entity -> target = entity }
    }

    /**
     * Is called when [.shouldExecute] returns true
     */
    override fun execute() {
        pathfinderGoalEntity.navigation.moveTo(target, 2.0)
        pathfinderGoalEntity.controllerJump.jump()
    }


    override fun reset() {

        this.pathfinderGoalEntity.navigation.clearPathEntity()

        if (this.target != null) {
            if (this.target is Item) {
                val buEntity = this.pathfinderGoalEntity.bukkitEntity
                if (buEntity is LivingEntity) {
                    buEntity.equipment.itemInMainHand = (this.target as Item).itemStack
                }
                this.target!!.remove()
                this.target = null
            }
        }
    }

    private inner class DistanceComperator : Comparator<Entity> {

        override fun compare(o1: Entity, o2: Entity): Int {
            return java.lang.Double.compare(getDistance(o1), getDistance(o2))
        }

        private fun getDistance(entity: Entity): Double {
            return entity.location
                    .distanceSquared(pathfinderGoalEntity.bukkitEntity.location)
        }

    }

}