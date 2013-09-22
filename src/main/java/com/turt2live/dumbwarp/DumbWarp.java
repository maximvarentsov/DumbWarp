package com.turt2live.dumbwarp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DumbWarp extends JavaPlugin{

	public static DumbWarp p;

	private static Map<String, Warp> warps = new HashMap<String, Warp>();

	/**
	 * Adds a warp to the plugin
	 * 
	 * @param name the warp name, spaces and other invalid characters are removed
	 * @param warp the warp
	 * @return true if the warp was added, false otherwise
	 */
	public static boolean addWarp(String name, Warp warp){
		if(name != null){
			name = name.trim();
			name = name.replaceAll(" ", "");
		}
		if(name == null || warp == null || name.length() == 0){
			throw new IllegalArgumentException("NO! I DO NOT WANT NULL. GIMMEH REAL OBJECTS.");
		}
		if(warpExists(name)){
			return false;
		}else{
			warps.put(name.toLowerCase(), warp);
			return true;
		}
	}

	/**
	 * Determines if a warp name exists
	 * 
	 * @param name the name to look for. Spaces and other invalid characters are removed.
	 * @return true if the warp name exists, false otherwise
	 */
	public static boolean warpExists(String name){
		if(name != null){
			name = name.trim();
			name = name.replaceAll(" ", "");
			if(name.length() > 0){
				return warps.containsKey(name.toLowerCase());
			}
		}
		return false;
	}

	/**
	 * Removes a warp by name
	 * 
	 * @param name the warp name to remove. Spaces and other invalid characters are removed
	 * @return true if the warp was removed, false otherwise
	 */
	public static boolean removeWarp(String name){
		if(name != null){
			name = name.trim();
			name = name.replaceAll(" ", "");
		}
		if(name == null || name.length() == 0){
			throw new IllegalArgumentException("NO! I DO NOT WANT NULL. GIMMEH REAL OBJECTS.");
		}
		return warps.remove(name.toLowerCase()) != null;
	}

	/**
	 * Removes a warp by name
	 * 
	 * @param name the name to remove. Spaces and other invalid characters are removed
	 * @return the warp found, null if no warp by that name is found
	 */
	public static Warp getWarp(String name){
		if(name != null){
			name = name.trim();
			name = name.replaceAll(" ", "");
		}
		if(name == null || name.length() == 0){
			throw new IllegalArgumentException("NO! I DO NOT WANT NULL. GIMMEH REAL OBJECTS.");
		}
		return warps.get(name.toLowerCase());
	}

	/**
	 * Gets a listing of all warps known
	 * 
	 * @return a listing of all the known warps
	 */
	public static List<WarpInfo> getAllWarps(){
		List<WarpInfo> warps = new ArrayList<WarpInfo>();
		for(String name : DumbWarp.warps.keySet()){
			Warp warp = DumbWarp.warps.get(name);
			WarpInfo warpEntry = new WarpInfo(name, warp);
			warps.add(warpEntry);
		}
		return warps;
	}

	@Override
	public void onEnable(){
		p = this;

		saveDefaultConfig();

		// Load warps
		for(String warpName : getConfig().getKeys(false)){
			String world = getConfig().getString(warpName + ".world");
			double x = getConfig().getDouble(warpName + ".x");
			double y = getConfig().getDouble(warpName + ".y");
			double z = getConfig().getDouble(warpName + ".z");
			float pitch = (float) getConfig().getDouble(warpName + ".pitch");
			float yaw = (float) getConfig().getDouble(warpName + ".yaw");

			warps.put(warpName.toLowerCase(), new Warp(world, x, y, z, pitch, yaw));
		}

		getLogger().info(warps.size() + " warps loaded!");
		getLogger().info("Enabled! (DumbWarp v" + getDescription().getVersion() + " ~~ Created by turt2live)");
	}

	@Override
	public void onDisable(){
		p = null;

		// First: Delete all warps, so we can cleanup old ones
		List<String> del = new ArrayList<String>();
		for(String key : getConfig().getKeys(false)){
			del.add(key);
		}
		for(String key : del){
			getConfig().set(key, null);
		}

		// Save warps
		for(String name : warps.keySet()){
			warps.get(name).save(name, getConfig());
		}
		saveConfig();

		warps.clear(); // Just in case
		getLogger().info("Disabled! (DumbWarp v" + getDescription().getVersion() + " ~~ Created by turt2live)");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(command.getName().equalsIgnoreCase("setwarp")){
			if(!sender.hasPermission("DumbWarp.set")){
				sendMessage(sender, ChatColor.RED + "No permission.");
			}else{
				if(sender instanceof Player){
					if(args.length <= 0){
						sendMessage(sender, ChatColor.RED + "Incorrect syntax. Did you mean " + ChatColor.YELLOW + "/setwarp <name>" + ChatColor.RED + "?");
					}else{
						Player player = (Player) sender;
						Location here = player.getLocation();
						Warp warp = new Warp(here);
						String name = args[0].toLowerCase();
						if(warps.containsKey(name)){
							sendMessage(sender, ChatColor.RED + "Name taken.");
						}else{
							warps.put(name, warp);
							sendMessage(sender, ChatColor.GREEN + "Warp '" + name + "' created!");
						}
					}
				}else{
					sendMessage(sender, ChatColor.RED + "No");
				}
			}
		}else if(command.getName().equalsIgnoreCase("delwarp")){
			if(!sender.hasPermission("DumbWarp.del")){
				sendMessage(sender, ChatColor.RED + "No permission.");
			}else{
				if(args.length <= 0){
					sendMessage(sender, ChatColor.RED + "Incorrect syntax. Did you mean " + ChatColor.YELLOW + "/delwarp <name>" + ChatColor.RED + "?");
				}else{
					String name = args[0].toLowerCase();
					if(warps.containsKey(name)){
						warps.remove(name);
						sendMessage(sender, ChatColor.GREEN + "Warp '" + name + "' removed!");
					}else{
						sendMessage(sender, ChatColor.RED + "Warp '" + name + "' not found.");
					}
				}
			}
		}else if(command.getName().equalsIgnoreCase("warp")){
			if(!sender.hasPermission("DumbWarp.warp")){
				sendMessage(sender, ChatColor.RED + "No permission.");
			}else{
				if(sender instanceof Player){
					Player player = (Player) sender;
					if(args.length <= 0){
						sendMessage(sender, ChatColor.RED + "Incorrect syntax. Did you mean " + ChatColor.YELLOW + "/warp <name>" + ChatColor.RED + "?");
					}else{
						String name = args[0].toLowerCase();
						if(warps.containsKey(name)){
							Warp warp = warps.get(name);
							Location l = warp.toBukkit();
							sendMessage(sender, ChatColor.GRAY + "Whoosh!");
							player.teleport(l);
						}else{
							sendMessage(sender, ChatColor.RED + "Warp '" + name + "' not found.");
						}
					}
				}else{
					sendMessage(sender, ChatColor.RED + "No.");
				}
			}
		}else if(command.getName().equalsIgnoreCase("warps")){
			if(!sender.hasPermission("DumbWarp.list")){
				sendMessage(sender, ChatColor.RED + "No permission.");
			}else{
				List<WarpInfo> warps = getAllWarps();
				int startAt = 0;
				if(args.length > 0){
					try{
						int i = Integer.parseInt(args[0]) - 1;
						if(i < 0){
							i = 0;
						}
						startAt = i * 8;
					}catch(NumberFormatException e){
						sendMessage(sender, ChatColor.RED + "Incorrect syntax. Try " + ChatColor.YELLOW + "/warps <page number>" + ChatColor.RED + ".");
						return true;
					}
				}
				int pages = (int) Math.ceil(warps.size() / 8.0);
				int page = (int) (startAt / 8.0) + 1;
				if(page > pages){
					sendMessage(sender, ChatColor.RED + "Page out of range.");
				}else{
					sendMessage(sender, ChatColor.DARK_GREEN + "====[ " + ChatColor.GREEN + "Known Warps " + ChatColor.DARK_GREEN + "|" + ChatColor.GREEN + " Page " + page + "/" + pages + ChatColor.DARK_GREEN + " ]====");
					for(int i = startAt; i < (startAt + 8); i++){
						if(i < warps.size()){
							WarpInfo warpInfo = warps.get(i);
							Warp warp = warpInfo.getWarp();
							sendMessage(sender, ChatColor.AQUA + warpInfo.getName() + " " + ChatColor.GRAY + "(" + warp.getWorld() + ", " + round(warp.getX(), 2) + ", " + round(warp.getY(), 2) + ", " + round(warp.getZ(), 2) + ")");
						}else{
							break;
						}
					}
					sendMessage(sender, ChatColor.DARK_GREEN + "====[ " + ChatColor.GREEN + "Known Warps " + ChatColor.DARK_GREEN + "|" + ChatColor.GREEN + " Page " + page + "/" + pages + ChatColor.DARK_GREEN + " ]====");
				}
			}
		}else{
			sendMessage(sender, ChatColor.RED + "Something broke.");
		}
		return true;
	}

	private void sendMessage(CommandSender sender, String message){
		sender.sendMessage(ChatColor.GRAY + "[DumbWarp] " + ChatColor.WHITE + message);
	}

	private double round(double v, int n){
		return new BigDecimal(v).setScale(n, RoundingMode.HALF_EVEN).doubleValue();
	}

}
