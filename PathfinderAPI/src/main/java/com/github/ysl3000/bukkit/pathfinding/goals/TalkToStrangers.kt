package com.github.ysl3000.bukkit.pathfinding.goals

import java.util.stream.Collectors

import org.bukkit.entity.Player

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal

/**
 * Created by ysl3000
 */
class TalkToStrangers(private val insentient: Insentient, private val messages: List<String>, private val delay: Long) : PathfinderGoal {


    private val radius: Int

    private var currentMessage: Iterator<String>? = null
    private var lastTalk: Long = 0


    private val isAllowedToTalk: Boolean
        get() = lastTalk + delay < System.currentTimeMillis()


    private val nearbyPlayers: List<Player>
        get() = insentient.bukkitEntity.getNearbyEntities(radius.toDouble(), radius.toDouble(), radius.toDouble()).stream()
                .filter(Predicate<Entity> { Player::class.java!!.isInstance(it) }).map<Player>(Function<Entity, Player> { Player::class.java!!.cast(it) }).collect<List<Player>, Any>(Collectors.toList())

    init {
        this.radius = 10
        this.currentMessage = messages.iterator()
    }


    override fun shouldExecute(): Boolean {
        return isAllowedToTalk && hasPlayerInRadius() && (currentMessage == null || currentMessage!!
                .hasNext())
    }


    override fun shouldTerminate(): Boolean {
        return currentMessage!!.hasNext()
    }

    override fun init() {
        if (currentMessage == null) {
            currentMessage = messages.iterator()
        }
    }

    override fun execute() {
        if (currentMessage!!.hasNext() && isAllowedToTalk) {
            val cMessage = currentMessage!!.next()
            nearbyPlayers.forEach { e -> e.sendMessage(cMessage) }
            this.lastTalk = System.currentTimeMillis()
        }
    }

    override fun reset() {
        currentMessage = null
    }

    private fun hasPlayerInRadius(): Boolean {
        return nearbyPlayers.size > 0
    }


}
