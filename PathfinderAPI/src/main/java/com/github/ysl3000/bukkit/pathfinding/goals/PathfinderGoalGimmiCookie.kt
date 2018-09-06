package com.github.ysl3000.bukkit.pathfinding.goals

import java.util.Comparator

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal

/**
 * Created by 2008Choco
 */
class PathfinderGoalGimmiCookie(private val insentient: Insentient) : PathfinderGoal {

    private var isGenerous = true
    private var hasGivenCookie = false

    private var nearbyEntities: List<Entity>? = null
    private var nearestPlayer: Player? = null

    override fun shouldExecute(): Boolean {
        if (!isGenerous) {
            return false
        }

        val nearbyEntities = insentient.bukkitEntity.getNearbyEntities(5.0, 5.0, 5.0)
        if (nearbyEntities.isEmpty()) {
            return false
        }

        this.nearbyEntities = nearbyEntities

        return nearbyEntities.stream()
                .anyMatch { entity -> entity.type == EntityType.PLAYER }
    }

    override fun shouldTerminate(): Boolean {
        return !hasGivenCookie
    }

    override fun init() {
        this.nearestPlayer = getNearestPlayerFrom(nearbyEntities!!)
        this.nearbyEntities = null
        val entity = this.insentient.bukkitEntity as Creature
        entity.getEquipment().setItemInMainHand(COOKIE)
        entity.setTarget(nearestPlayer)
        this.insentient.navigation.moveTo(nearestPlayer!!)
    }

    override fun reset() {
        this.nearestPlayer = null
        this.hasGivenCookie = false
    }

    override fun execute() {
        this.insentient.jump()
        val creature = insentient.bukkitEntity as Creature

        if (creature.getLocation().distanceSquared(nearestPlayer!!.location) <= 1) {
            creature.getWorld().dropItem(nearestPlayer!!.location, COOKIE)
            creature.getEquipment().setItemInMainHand(null)
            this.isGenerous = false
            this.hasGivenCookie = true
        }
    }

    private fun getNearestPlayerFrom(entities: List<Entity>): Player? {
        val zombieLocation = insentient.bukkitEntity.location
        return entities.stream()
                .filter { e -> e is Player }
                .min(Comparator.comparingDouble { p -> zombieLocation.distanceSquared(p.location) })
                .orElse(null) as Player
    }

    companion object {

        private val COOKIE = ItemStack(Material.COOKIE)
    }

}