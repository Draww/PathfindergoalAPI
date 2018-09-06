package com.github.ysl3000.bukkit.pathfinding.pathfinding

import org.bukkit.entity.Mob

interface FeaturedOathfinderManager : PathfinderManager {
    /**
     * Returns a pathfinderGoalEntity from creature
     *
     * @param creature entity you want to get the PathfinderGoalEntity
     * @return pathfinderGoalEntity the entity you can apply pathfindergoals on to
     */
    fun getPathfindeGoalEntity(mob: Mob)
}