package com.github.ysl3000.bukkit.pathfinding.pathfinding

import org.bukkit.Location
import org.bukkit.entity.Entity

import com.github.ysl3000.bukkit.pathfinding.entity.Insentient

/**
 * @Deprecation {[com.github.ysl3000.bukkit.pathfinding.entity.Insentient]} Created by Yannick
 * on 30.11.2016.
 */
@Deprecated("")
interface ControllerLook {

    /**
     * Checks if the looking is reset
     *
     * @return if looking is default
     */
    @get:Deprecated("")
    val isReset: Boolean

    /**
     * Use
     *
     * @return x-location
     * @Deprecation {[Insentient.getLookingAt]}
     *
     * Gets the X-Location the entity is looking to
     */
    @get:Deprecated("")
    val locationX: Double

    /**
     * Use
     *
     * @return y-location
     * @Deprecation {[Insentient.getLookingAt]}
     *
     * Gets the Y-Location the entity is looking to
     */
    @get:Deprecated("")
    val locationY: Double

    /**
     * Use
     *
     * @return z-location
     * @Deprecation {[Insentient.getLookingAt]}
     *
     * Gets the Z-Location the entity is looking to
     */
    @get:Deprecated("")
    val locationZ: Double

    /**
     * * Use
     *
     * @param x x-location
     * @param y y-location
     * @param z z-location
     * @param pitch head rotation pitch
     * @param yaw head rotation yaw
     * @Deprecation {[com.github.ysl3000.bukkit.pathfinding.entity.Insentient.lookAt]}
     *
     * The entity will look to the given coordinates
     */
    @Deprecated("")
    fun lookAt(x: Double, y: Double, z: Double, yaw: Float, pitch: Float)

    /**
     * Use
     *
     * @param location the entity should look to
     * @Deprecation {[com.github.ysl3000.bukkit.pathfinding.entity.Insentient.lookAt]}
     *
     * The entity will look to the given location
     */
    @Deprecated("")
    fun lookAt(location: Location)

    /**
     * Use
     *
     * @param entity the entity which is targeted with eyes
     * @param pitch head rotation pitch
     * @param yaw head rotation yaw
     * @Deprecation {[com.github.ysl3000.bukkit.pathfinding.entity.Insentient.lookAt]}
     * The entity will look to the given entity
     */
    @Deprecated("")
    fun lookAt(entity: Entity, yaw: Float, pitch: Float)

    /**
     * Resets the current looking
     */
    @Deprecated("")
    fun reset()
}