package com.github.ysl3000.bukkit.pathfinding.pathfinding;

@Deprecated
public interface ControllerMove {

  boolean isOperationMove();

  void move(double motionX, double motionY, double motionZ, double speed);

  void move(float forward, float sideward);

  void update();


  double getX();

  double getY();

  double getZ();

  double getSpeed();


}
