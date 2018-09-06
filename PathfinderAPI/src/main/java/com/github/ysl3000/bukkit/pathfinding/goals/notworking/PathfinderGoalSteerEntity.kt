package com.github.ysl3000.bukkit.pathfinding.goals.notworking

import java.util.Optional

import org.bukkit.Location
import org.bukkit.entity.Player

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderManager
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderPlayer

class PathfinderGoalSteerEntity(private val pathfinderManager: PathfinderManager,
                                private val pathfinderGoalEntity: Insentient) : PathfinderGoal {


    private var pathfinderPlayer: PathfinderPlayer? = null

    private val steeringPlayer: Optional<Player>
        get() = pathfinderGoalEntity.bukkitEntity.passengers.stream()
                .filter(Predicate<Entity> { Player::class.java!!.isInstance(it) }).map<Player>(Function<Entity, Player> { Player::class.java!!.cast(it) }).findAny()


    override fun shouldExecute(): Boolean {
        return pathfinderPlayer == null || steeringPlayer.isPresent
    }

    override fun shouldTerminate(): Boolean {
        return steeringPlayer.isPresent
    }

    override fun init() {

        if (pathfinderPlayer == null) {
            steeringPlayer.ifPresent { player -> this.pathfinderPlayer = pathfinderManager.getPathfinderPlayer(player) }
        }

    }


    override fun execute() {

        if (pathfinderPlayer == null) {
            return
        }

        val player = pathfinderPlayer!!.player

        val blockInDirection = player.location
                .add(player.location.direction.setY(0).normalize().multiply(1))

        if (blockInDirection.block.type.isSolid && !blockInDirection.add(0.0, 1.0, 0.0).block
                        .type.isSolid) {
            pathfinderGoalEntity.controllerJump.jump()
        }

        this.pathfinderGoalEntity.controllerMove
                .move(pathfinderPlayer!!.motionForward, pathfinderPlayer!!.motionSideward)
        val location = player.location

        this.pathfinderGoalEntity.setRotation(location.yaw, location.yaw)
    }

    override fun reset() {
        this.pathfinderPlayer = null
    }
}