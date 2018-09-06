package com.github.ysl3000.bukkit.pathfinding

import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderManager

object PathfinderGoalAPI {

    var api: PathfinderManager? = null
        set(manager) {
            if (field != null) {
                throw UnsupportedOperationException("Cannot redefine singleton Server")
            } else {
                field = manager
            }
        }
}
