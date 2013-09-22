package com.turt2live.dumbwarp;

import org.bukkit.Location;

/**
 * Represents a warp
 * 
 * @author turt2live
 */
public class Warp{

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
	public Warp(String worldName, double x, double y, double z, float pitch, float yaw){
		if(worldName == null){
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
	public Warp(Location location){
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
	public Location toBukkit(){
		return new Location(DumbWarp.p.getServer().getWorld(world), x, y, z, yaw, pitch);
	}

}
