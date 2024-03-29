/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.dumbwarp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Represents a warp
 *
 * @author turt2live
 */
public class Warp {

    private String world;
    private double x, y, z;
    private float pitch, yaw;
    private String name;

    /**
     * Creates a new warp
     *
     * @param warpName  the warp name
     * @param worldName the world name
     * @param x         the x location
     * @param y         the y location
     * @param z         the z location
     * @param pitch     the pitch
     * @param yaw       the yaw
     */
    public Warp(String warpName, String worldName, double x, double y, double z, float pitch, float yaw) {
        if (worldName == null || warpName == null) {
            throw new IllegalArgumentException("No dumb names");
        }
        this.name = warpName;
        this.world = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Creates a new warp
     *
     * @param warpName the warp name
     * @param location the location for the warp
     */
    public Warp(String warpName, Location location) {
        if (location == null || warpName == null) {
            throw new IllegalArgumentException("No dumb names");
        }
        this.name = warpName;
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    /**
     * Converts this location to a Bukkit location
     *
     * @return the Bukkit location
     */
    public Location toBukkit() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    /**
     * Saves the warp to a configuration file.
     *
     * @param config The config file to save to
     */
    public void save(FileConfiguration config) {
        config.set(name + ".world", world);
        config.set(name + ".x", x);
        config.set(name + ".y", y);
        config.set(name + ".z", z);
        config.set(name + ".pitch", pitch);
        config.set(name + ".yaw", yaw);
    }

    /**
     * Gets the world associated with this warp
     *
     * @return the world name
     */
    public String getWorld() {
        return world;
    }

    /**
     * Gets the x coordinate associated with this warp
     *
     * @return the x value
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y coordinate associated with this warp
     *
     * @return the y value
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the z coordinate associated with this warp
     *
     * @return the z value
     */
    public double getZ() {
        return z;
    }

    /**
     * Gets the pitch associated with this warp
     *
     * @return the pitch
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Gets the yaw associated with this warp
     *
     * @return the yaw
     */
    public float getYaw() {
        return yaw;
    }

    public String getName() {
        return name;
    }
}
