package com.github.ysl3000.bukkit.pathfinding.craftbukkit.v1_12_R1.entity

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.HashMap

import org.bukkit.Location
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAmbient
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderDragon
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFlying
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSlime
import org.bukkit.entity.Ambient
import org.bukkit.entity.Creature
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Entity
import org.bukkit.entity.Flying
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Slime
import org.bukkit.util.Vector

import com.github.ysl3000.bukkit.pathfinding.craftbukkit.v1_12_R1.pathfinding.CraftNavigation
import com.github.ysl3000.bukkit.pathfinding.craftbukkit.v1_12_R1.pathfinding.CraftPathfinderGoalWrapper
import com.github.ysl3000.bukkit.pathfinding.entity.Insentient

import net.minecraft.server.v1_12_R1.ControllerLook
import net.minecraft.server.v1_12_R1.EntityInsentient
import net.minecraft.server.v1_12_R1.PathfinderGoal
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector

/**
 * Created by ysl3000
 */
class CraftInsentient private constructor(private val handle: EntityInsentient) : Insentient {


    private val nmsGoals = HashMap<com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal, PathfinderGoal>()
    private val nmsTargetGoals = HashMap<com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal, PathfinderGoal>()

    override val navigation: com.github.ysl3000.bukkit.pathfinding.pathfinding.Navigation

    override val lookingAt: Location
        get() {
            val controllerLook = handle.controllerLook
            return Location(handle.bukkitEntity.world, controllerLook.e(), controllerLook.f(),
                    controllerLook.g())
        }

    override val targetSelector: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoalSelector
        get() = TargetGoalSelector()

    override val goalSelector: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoalSelector
        get() = GoalSelector()

    override val controllerJump: com.github.ysl3000.bukkit.pathfinding.pathfinding.ControllerJump
        get() = ControllerJump()

    override val controllerLook: com.github.ysl3000.bukkit.pathfinding.pathfinding.ControllerLook
        get() = ControllerLookImpl()

    override val controllerMove: com.github.ysl3000.bukkit.pathfinding.pathfinding.ControllerMove
        get() = ControllerMove()

    override val headHeight: Float
        get() = handle.headHeight

    override val defaultYaw: Int
        get() = handle.O()

    override val defaultPitch: Int
        get() = handle.N()

    override val bukkitEntity: Entity
        get() = handle.bukkitEntity

    constructor(flying: Flying) : this((flying as CraftFlying).handle) {}

    init {
        this.navigation = CraftNavigation(handle.navigation)

    }


    constructor(enderDragon: EnderDragon) : this((enderDragon as CraftEnderDragon).handle) {}

    constructor(creature: Creature) : this((creature as CraftCreature).handle) {}

    constructor(ambient: Ambient) : this((ambient as CraftAmbient).handle) {}

    constructor(slime: Slime) : this((slime as CraftSlime).handle) {}

    override fun addPathfinderGoal(priority: Int,
                                   pathfinderGoal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal) {
        val goalWrapper = CraftPathfinderGoalWrapper(pathfinderGoal)
        this.nmsGoals[pathfinderGoal] = goalWrapper
        handle.goalSelector.a(priority, goalWrapper)
    }

    override fun removePathfinderGoal(
            pathfinderGoal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal) {
        if (nmsGoals.containsKey(pathfinderGoal)) {
            val nmsGoal = nmsGoals.remove(pathfinderGoal)
            handle.goalSelector.a(nmsGoal)
        }
    }

    override fun hasPathfinderGoal(
            pathfinderGoal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal): Boolean {
        return nmsGoals.containsKey(pathfinderGoal)
    }

    override fun clearPathfinderGoals() {
        handle.goalSelector = PathfinderGoalSelector(handle.getWorld().methodProfiler)
        nmsGoals.clear()
    }


    override fun addTargetPathfinderGoal(priority: Int,
                                         pathfinderGoal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal) {
        val goalWrapper = CraftPathfinderGoalWrapper(pathfinderGoal)
        this.nmsTargetGoals[pathfinderGoal] = goalWrapper
        handle.targetSelector.a(priority, goalWrapper)
    }

    override fun removeTargetPathfinderGoal(
            pathfinderGoal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal) {
        if (nmsTargetGoals.containsKey(pathfinderGoal)) {
            val nmsGoal = nmsTargetGoals.remove(pathfinderGoal)
            handle.goalSelector.a(nmsGoal)
        }
    }

    override fun hasTargetPathfinderGoal(
            pathfinderGoal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal): Boolean {
        return nmsTargetGoals.containsKey(pathfinderGoal)
    }

    override fun clearTargetPathfinderGoals() {
        handle.targetSelector = PathfinderGoalSelector(handle.getWorld().methodProfiler)
        nmsTargetGoals.clear()
    }

    override fun jump() {
        handle.controllerJump.a()
    }

    /**
     * The entity will look to the given location
     *
     * @param location the entity should look to
     */
    override fun lookAt(location: Location) {
        handle.controllerLook
                .a(location.x, location.y, location.z, location.yaw,
                        location.pitch)
    }

    /**
     * The entity will look to the given entity
     *
     * @param entity the entity which is targeted with eyes
     */
    override fun lookAt(entity: Entity) {
        lookAt(entity.location)
    }

    override fun setMovementDirection(direction: Vector, speed: Double) {
        handle.controllerMove.a(direction.x, direction.blockY.toDouble(), direction.z, speed)
    }

    override fun setStrafeDirection(forward: Float, sideward: Float) {
        handle.controllerMove.a(forward, sideward)
    }

    override fun resetGoalsToDefault() {
        if (reset == null) {
            return
        }
        try {
            reset!!.invoke(handle)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }

    override fun hasPositionChanged(): Boolean {
        return handle.positionChanged
    }

    override fun onEntityKill(livingEntity: LivingEntity) {
        handle.b(if (livingEntity == null) null else (livingEntity as CraftLivingEntity).handle)
    }

    override fun moveRelative(motionX: Double, motionY: Double, motionZ: Double) {
        moveRelative(motionX, motionY, motionZ, 1.0)
    }

    override fun moveRelative(motionX: Double, motionY: Double, motionZ: Double, speed: Double) {
        this.handle.motX += motionX * speed
        this.handle.motY += motionY * speed
        this.handle.motZ += motionZ * speed
        this.handle.recalcPosition()
    }

    override fun setRotation(yaw: Float, pitch: Float) {
        this.handle.yaw = yaw
        this.handle.pitch = pitch
    }

    override fun updateRenderAngles() {
        handle.controllerMove.a()
    }


    private inner class GoalSelector : com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoalSelector {

        /**
         * Add a pathfinder goal to the entity
         *
         * @param priority the priority 0 highest
         * @param goal The goal to add
         */
        override fun addPathfinderGoal(priority: Int,
                                       goal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal) {
            this@CraftInsentient.addPathfinderGoal(priority, goal)
        }

        /**
         * Remove a pathfinder goal from the entity
         *
         * @param goal The goal to remove
         */
        override fun removePathfinderGoal(
                goal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal) {
            this@CraftInsentient.removePathfinderGoal(goal)
        }

        /**
         * Will clear all goals
         */
        override fun clearGoals() {
            this@CraftInsentient.clearPathfinderGoals()
        }
    }

    private inner class TargetGoalSelector : com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoalSelector {

        /**
         * Add a pathfinder goal to the entity
         *
         * @param priority the priority 0 highest
         * @param goal The goal to add
         */
        override fun addPathfinderGoal(priority: Int,
                                       goal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal) {
            this@CraftInsentient.addTargetPathfinderGoal(priority, goal)
        }

        /**
         * Remove a pathfinder goal from the entity
         *
         * @param goal The goal to remove
         */
        override fun removePathfinderGoal(
                goal: com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal) {
            this@CraftInsentient.removeTargetPathfinderGoal(goal)
        }

        /**
         * Will clear all goals
         */
        override fun clearGoals() {
            this@CraftInsentient.clearTargetPathfinderGoals()
        }
    }

    private inner class ControllerJump : com.github.ysl3000.bukkit.pathfinding.pathfinding.ControllerJump {

        /**
         * Lets the entity jump
         */
        override fun jump() {
            this@CraftInsentient.jump()
        }
    }

    private inner class ControllerMove : com.github.ysl3000.bukkit.pathfinding.pathfinding.ControllerMove {

        override val isOperationMove: Boolean
            get() = handle.controllerMove.h == net.minecraft.server.v1_12_R1.ControllerMove.Operation.MOVE_TO

        override val x: Double
            get() = handle.controllerMove.c()

        override val y: Double
            get() = handle.controllerMove.d()

        override val z: Double
            get() = handle.controllerMove.e()

        override val speed: Double
            get() = handle.controllerMove.f()

        override fun move(motionX: Double, motionY: Double, motionZ: Double, speed: Double) {
            this@CraftInsentient.setMovementDirection(Vector(motionX, motionY, motionZ), speed)
        }

        override fun move(forward: Float, sideward: Float) {
            this@CraftInsentient.setStrafeDirection(forward, sideward)
        }

        override fun update() {
            handle.controllerMove.a()
        }
    }

    private inner class ControllerLookImpl : com.github.ysl3000.bukkit.pathfinding.pathfinding.ControllerLook {

        /**
         * Checks if the looking is reset
         *
         * @return if looking is default
         */
        override val isReset: Boolean
            get() = handle.controllerLook.b()

        /**
         * Gets the X-Location the entity is looking to
         *
         * @return x-location
         */
        override val locationX: Double
            get() = handle.controllerMove.c()

        /**
         * Gets the Y-Location the entity is looking to
         *
         * @return y-location
         */
        override val locationY: Double
            get() = handle.controllerMove.d()

        /**
         * Gets the Z-Location the entity is looking to
         *
         * @return z-location
         */
        override val locationZ: Double
            get() = handle.controllerMove.f()

        /**
         * The entity will look to the given coordinates
         *
         * @param x x-location
         * @param y y-location
         * @param z z-location
         * @param yaw head rotation yaw
         * @param pitch head rotation pitch
         */
        override fun lookAt(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
            this@CraftInsentient
                    .lookAt(Location(handle.bukkitEntity.world, x, y, z, yaw, pitch))
        }

        /**
         * The entity will look to the given location
         *
         * @param location the entity should look to
         */
        override fun lookAt(location: Location) {
            this@CraftInsentient.lookAt(location)
        }

        /**
         * The entity will look to the given entity
         *
         * @param entity the entity which is targeted with eyes
         * @param yaw head rotation yaw
         * @param pitch head rotation pitch
         */
        override fun lookAt(entity: Entity, yaw: Float, pitch: Float) {
            this@CraftInsentient.lookAt(entity)
        }

        /**
         * Resets the current looking
         */
        override fun reset() {
            handle.controllerLook.a()
        }
    }

    companion object {


        private var reset: Method? = null

        init {

            try {
                reset = EntityInsentient::class.java.getDeclaredMethod("r")
                reset!!.isAccessible = true
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }

        }
    }

}
