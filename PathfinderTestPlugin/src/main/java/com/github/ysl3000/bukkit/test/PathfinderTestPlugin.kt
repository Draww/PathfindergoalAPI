package com.github.ysl3000.bukkit.test

import com.github.ysl3000.bukkit.pathfinding.PathfinderGoalAPI
import com.github.ysl3000.bukkit.pathfinding.entity.Insentient
import com.github.ysl3000.bukkit.pathfinding.goals.PathfinderGoalGimmiCookie
import com.github.ysl3000.bukkit.pathfinding.goals.PathfinderGoalMoveToLocation
import com.github.ysl3000.bukkit.pathfinding.goals.TalkToStrangers
import com.github.ysl3000.bukkit.pathfinding.goals.notworking.PathfinderGoalFollowEntity
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Creature
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

/**
 * Created by ysl3000
 */
class PathfinderTestPlugin : JavaPlugin(), Listener {

    private val commandMap = HashMap<String, ICommand>()

    override fun onEnable() {

        val pathfinderManager = PathfinderGoalAPI.api

        commandMap["chat"] = Chat(pathfinderManager)
        commandMap["cookie"] = DeliverCookie(pathfinderManager)
        commandMap["move"] = MoveToLocation(pathfinderManager)
        commandMap["follow"] = FollowOwner(pathfinderManager)
        commandMap["print"] = PrintGoal(pathfinderManager)

        Bukkit.getServer().pluginManager.registerEvents(this, this)
    }


    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {

        val player = event.player

        val args = Arrays.asList(*event.message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())

        if (!args.isEmpty()) {

            val command = args[0]

            val iCommand = commandMap[command]

            var param: List<String> = ArrayList()

            if (args.size > 1) {
                param = args.subList(1, args.size)
            }

            val finalParam = param
            Bukkit.getScheduler().runTask(this) {

                iCommand?.execute(player, finalParam)

            }
        }
    }

    private inner class Chat private constructor(private val pathfinderManager: PathfinderManager) : ICommand {


        override fun execute(player: Player, args: List<String>): Boolean {

            val creature = player.world.spawn(player.location, Zombie::class.java)
            val pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature)

            pathfinderGoalEntity.clearPathfinderGoals()
            pathfinderGoalEntity.clearTargetPathfinderGoals()
            pathfinderGoalEntity.addPathfinderGoal(0,
                    TalkToStrangers(pathfinderGoalEntity, args, TimeUnit.SECONDS.toMillis(20)))
            return true
        }
    }

    private inner class DeliverCookie private constructor(private val pathfinderManager: PathfinderManager) : ICommand {

        override fun execute(player: Player, args: List<String>): Boolean {

            val rand = ThreadLocalRandom.current()

            val spawn = player.location.add(rand.nextDouble(10.0), 0.0, rand.nextDouble(10.0))


            val creature = player.world
                    .spawn(spawn, Zombie::class.java)
            val pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature)

            pathfinderGoalEntity.clearPathfinderGoals()
            pathfinderGoalEntity.clearTargetPathfinderGoals()
            pathfinderGoalEntity
                    .addPathfinderGoal(0, PathfinderGoalGimmiCookie(pathfinderGoalEntity))

            return true
        }
    }

    private inner class MoveToLocation private constructor(private val pathfinderManager: PathfinderManager) : ICommand {

        override fun execute(player: Player, args: List<String>): Boolean {

            val creature = player.world.spawn(player.location, Zombie::class.java)
            val pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature)
            pathfinderGoalEntity.clearPathfinderGoals()
            pathfinderGoalEntity.clearTargetPathfinderGoals()
            pathfinderGoalEntity.addPathfinderGoal(0,
                    PathfinderGoalMoveToLocation(
                            pathfinderGoalEntity, Location(player.world, 235.0, 70.0, 246.0),
                            2.0, 0.0)
            )

            return true
        }
    }

    private inner class FollowOwner private constructor(private val pathfinderManager: PathfinderManager) : ICommand {

        override fun execute(player: Player, args: List<String>): Boolean {

            val creature = player.world.spawn(player.location, Zombie::class.java)
            val pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature)
            pathfinderGoalEntity.clearPathfinderGoals()
            pathfinderGoalEntity.clearTargetPathfinderGoals()
            pathfinderGoalEntity.addPathfinderGoal(0,
                    PathfinderGoalFollowEntity(pathfinderGoalEntity, player, 5.0, 0.2)
            )

            return true
        }
    }


    private inner class PrintGoal(private val pathfinderManager: PathfinderManager) : ICommand {

        override fun execute(p: Player, args: List<String>): Boolean {
            val creature = p.world.spawn(p.location, Zombie::class.java)

            val pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature)
            pathfinderGoalEntity.clearPathfinderGoals()
            pathfinderGoalEntity.clearTargetPathfinderGoals()
            pathfinderGoalEntity.addPathfinderGoal(0,
                    PathfinderGoalPrint()
            )

            return true
        }

        private inner class PathfinderGoalPrint : PathfinderGoal {

            private var shE = true
            private var shT = true

            override fun shouldExecute(): Boolean {
                println("called should execute")
                return shE = !shE
            }

            override fun shouldTerminate(): Boolean {
                println("Called should terminate")
                return shT = !shT
            }

            override fun init() {
                println("Called Init")
            }

            override fun execute() {
                println("Called execute")
            }

            override fun reset() {
                println("Called reset")
            }
        }
    }
}
