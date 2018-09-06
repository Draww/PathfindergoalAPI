package com.github.ysl3000.bukkit.pathfinding

import java.util.logging.Level

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderManager

class PathfinderGoalPlugin : JavaPlugin() {

    private var craftPathfinderManager: PathfinderManager? = null

    override fun onDisable() {

        this.craftPathfinderManager = null

    }

    override fun onEnable() {

        val version = Bukkit.getServer().javaClass.getPackage().name.replace(".", ",")
                .split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[3]

        // TODO version version differentiation

        logger.log(Level.INFO, "Loading version $version")

        when (version) {

            "v1_12_R1" -> this.craftPathfinderManager = com.github.ysl3000.bukkit.pathfinding.craftbukkit.v1_12_R1.pathfinding.CraftPathfinderManager()
            "v1_13_R1" -> this.craftPathfinderManager = com.github.ysl3000.bukkit.pathfinding.craftbukkit.v1_13_R1.pathfinding.CraftPathfinderManager()

            else -> {
                println("This version of Minecraft is not supported")
                Bukkit.getPluginManager().disablePlugin(this)
            }
        }
        PathfinderGoalAPI.api = this.craftPathfinderManager
    }
}
