package com.github.ysl3000.bukkit.pathfinding;

import com.github.ysl3000.bukkit.pathfinding.craftbukkit.v1_12_R1.pathfinding.CraftPathfinderManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PathfinderGoalPlugin extends JavaPlugin {

    private CraftPathfinderManager craftPathfinderManager;

    @Override
    public void onDisable() {

        this.craftPathfinderManager=null;

    }

    @Override
    public void onEnable() {

        this.craftPathfinderManager = new CraftPathfinderManager();
        PathfinderGoalAPI.setApi(this.craftPathfinderManager);

    }
}
