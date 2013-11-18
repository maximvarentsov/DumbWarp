package com.turt2live.dumbwarp;

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

	/**
	 * Creates a new warp
	 * 
	 * @param worldName the world name
	 * @param x the x location
	 * @param y the y location
	 * @param z the z location
	 * @param pitch the pitch
	 * @param yaw the yaw
	 */
	public Warp(String worldName, double x, double y, double z, float pitch, float yaw) {
		if (worldName == null) {
			throw new IllegalArgumentException("No dumb names");
		}
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
	 * @param location the location for the warp
	 */
	public Warp(Location location) {
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
		return new Location(DumbWarp.p.getServer().getWorld(world), x, y, z, yaw, pitch);
	}

	/**
	 * Saves the warp to a configuration file.
	 * 
	 * @param name the name to save it under
	 * @param config The config file to save to
	 */
	public void save(String name, FileConfiguration config) {
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

}
