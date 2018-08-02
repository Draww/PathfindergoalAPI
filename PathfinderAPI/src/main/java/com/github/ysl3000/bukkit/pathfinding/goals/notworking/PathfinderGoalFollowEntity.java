package com.github.ysl3000.bukkit.pathfinding.goals.notworking;

import org.bukkit.entity.LivingEntity;

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient;
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal;

public class PathfinderGoalFollowEntity implements PathfinderGoal {


  private final LivingEntity entity;
  private final double moveRadius;
  private boolean isAlreadySet;
  private Insentient pathfinderGoalEntity;
  private double walkSpeed;


  public PathfinderGoalFollowEntity(Insentient pathfinderGoalEntity, LivingEntity entity,
      double moveRadius, double walkSpeed) {
    this.entity = entity;
    this.moveRadius = moveRadius;
    this.pathfinderGoalEntity = pathfinderGoalEntity;
    this.walkSpeed = walkSpeed;
  }

  public boolean shouldExecute() {
    return this.isAlreadySet = !this.pathfinderGoalEntity.getNavigation().isDoneNavigating();
  }

  /**
   * Whether the goal should Terminate
   *
   * @return true if should terminate
   */
  @Override
  public boolean shouldTerminate() {
    if (!this.isAlreadySet) {
      return true;
    }
    return this.pathfinderGoalEntity.getBukkitEntity().getLocation()
        .distance(this.entity.getLocation()) > this.moveRadius;
  }

  /**
   * Runs initially and should be used to setUp goalEnvironment Condition needs to be defined thus
   * your code in it isn't called
   */
  @Override
  public void init() {
    if (!this.isAlreadySet) {
      this.pathfinderGoalEntity.getNavigation().moveTo(this.entity, walkSpeed);
    }
  }

  /**
   * Is called when {@link #shouldExecute()} returns true
   */
  @Override
  public void execute() {
    this.pathfinderGoalEntity.getControllerJump().jump();
  }

  public void reset() {

  }
}