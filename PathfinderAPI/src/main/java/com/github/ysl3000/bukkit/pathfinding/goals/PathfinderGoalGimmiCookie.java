package com.github.ysl3000.bukkit.pathfinding.goals;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient;
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal;

/**
 * Created by 2008Choco
 */
public class PathfinderGoalGimmiCookie implements PathfinderGoal {

  private static final ItemStack COOKIE = new ItemStack(Material.COOKIE);

  private boolean isGenerous = true;
  private boolean hasGivenCookie = false;

  private List<Entity> nearbyEntities;
  private Player nearestPlayer;

  private final Insentient insentient;

  public PathfinderGoalGimmiCookie(Insentient zombie) {
    this.insentient = zombie;
  }

  @Override
  public boolean shouldExecute() {
    if (!isGenerous) {
      return false;
    }

    List<Entity> nearbyEntities = insentient.getBukkitEntity().getNearbyEntities(5, 5, 5);
    if (nearbyEntities.isEmpty()) {
      return false;
    }

    this.nearbyEntities = nearbyEntities;

    return nearbyEntities.stream()
        .anyMatch(entity -> entity.getType() == EntityType.PLAYER);
  }

  @Override
  public boolean shouldTerminate() {
    return !hasGivenCookie;
  }

  @Override
  public void init() {
    this.nearestPlayer = getNearestPlayerFrom(nearbyEntities);
    this.nearbyEntities = null;
    Creature entity = (Creature) this.insentient.getBukkitEntity();
    entity.getEquipment().setItemInMainHand(COOKIE);
    entity.setTarget(nearestPlayer);
    this.insentient.getNavigation().moveTo(nearestPlayer);
  }

  @Override
  public void reset() {
    this.nearestPlayer = null;
    this.hasGivenCookie = false;
  }

  @Override
  public void execute() {
    this.insentient.jump();
    Creature creature = (Creature) insentient.getBukkitEntity();

    if (creature.getLocation().distanceSquared(nearestPlayer.getLocation()) <= 1) {
      creature.getWorld().dropItem(nearestPlayer.getLocation(), COOKIE);
      creature.getEquipment().setItemInMainHand(null);
      this.isGenerous = false;
      this.hasGivenCookie = true;
    }
  }

  private Player getNearestPlayerFrom(List<Entity> entities) {
    Location zombieLocation = insentient.getBukkitEntity().getLocation();
    return (Player) entities.stream()
        .filter(e -> e instanceof Player)
        .min(Comparator.comparingDouble(p -> zombieLocation.distanceSquared(p.getLocation())))
        .orElse(null);
  }

}