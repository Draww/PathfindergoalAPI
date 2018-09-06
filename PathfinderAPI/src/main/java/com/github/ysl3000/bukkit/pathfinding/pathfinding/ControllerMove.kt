package com.github.ysl3000.bukkit.pathfinding.pathfinding

@Deprecated("")
interface ControllerMove {

    val isOperationMove: Boolean


    val x: Double

    val y: Double

    val z: Double

    val speed: Double

    fun move(motionX: Double, motionY: Double, motionZ: Double, speed: Double)

    fun move(forward: Float, sideward: Float)

    fun update()


}
