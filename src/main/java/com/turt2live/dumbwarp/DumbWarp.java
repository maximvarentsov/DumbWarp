package com.turt2live.dumbwarp;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DumbWarp extends JavaPlugin{

	public static DumbWarp p;

	private Map<String, Warp> warps = new HashMap<String, Warp>();

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
		}else{
			sendMessage(sender, ChatColor.RED + "Something broke.");
		}
		return true;
	}

	private void sendMessage(CommandSender sender, String message){
		sender.sendMessage(ChatColor.GRAY + "[DumbWarp] " + ChatColor.WHITE + message);
	}

}
