package com.turt2live.dumbwarp;

import com.turt2live.commonsense.DumbPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class DumbWarp extends DumbPlugin {

    private WarpManager manager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initCommonSense(66026);
        manager = new WarpManager();

        // Load warps
        for (String warpName : getConfig().getKeys(false)) {
            String world = getConfig().getString(warpName + ".world");
            double x = getConfig().getDouble(warpName + ".x");
            double y = getConfig().getDouble(warpName + ".y");
            double z = getConfig().getDouble(warpName + ".z");
            float pitch = (float) getConfig().getDouble(warpName + ".pitch");
            float yaw = (float) getConfig().getDouble(warpName + ".yaw");
            manager.addWarp(new Warp(warpName, world, x, y, z, pitch, yaw));
        }

        getLogger().info(manager.getWarps().size() + " warps loaded!");
    }

    @Override
    public void onDisable() {
        // First: Delete all warps, so we can cleanup old ones
        List<String> del = new ArrayList<String>();
        for (String key : getConfig().getKeys(false)) {
            del.add(key);
        }
        for (String key : del) {
            getConfig().set(key, null);
        }

        // Save warps
        for (Warp warp : manager.getWarps()) {
            warp.save(getConfig());
        }
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setwarp")) {
            if (!sender.hasPermission("DumbWarp.set")) {
                sendMessage(sender, ChatColor.RED + "No permission.");
            } else {
                if (sender instanceof Player) {
                    if (args.length <= 0) {
                        sendMessage(sender, ChatColor.RED + "Incorrect syntax. Did you mean " + ChatColor.YELLOW + "/setwarp <name>" + ChatColor.RED + "?");
                    } else {
                        Player player = (Player) sender;
                        Location here = player.getLocation();
                        String name = args[0].toLowerCase();
                        if (manager.hasWarp(name)) {
                            sendMessage(sender, ChatColor.RED + "Name taken.");
                        } else {
                            Warp warp = new Warp(name, here);
                            manager.addWarp(warp);
                            sendMessage(sender, ChatColor.GREEN + "Warp '" + name + "' created!");
                        }
                    }
                } else {
                    sendMessage(sender, ChatColor.RED + "No");
                }
            }
        } else if (command.getName().equalsIgnoreCase("delwarp")) {
            if (!sender.hasPermission("DumbWarp.del")) {
                sendMessage(sender, ChatColor.RED + "No permission.");
            } else {
                if (args.length <= 0) {
                    sendMessage(sender, ChatColor.RED + "Incorrect syntax. Did you mean " + ChatColor.YELLOW + "/delwarp <name>" + ChatColor.RED + "?");
                } else {
                    String name = args[0].toLowerCase();
                    if (manager.hasWarp(name)) {
                        manager.removeWarp(manager.getWarp(name));
                        sendMessage(sender, ChatColor.GREEN + "Warp '" + name + "' removed!");
                    } else {
                        sendMessage(sender, ChatColor.RED + "Warp '" + name + "' not found.");
                    }
                }
            }
        } else if (command.getName().equalsIgnoreCase("warp")) {
            if (!sender.hasPermission("DumbWarp.warp")) {
                sendMessage(sender, ChatColor.RED + "No permission.");
            } else {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length <= 0) {
                        sendMessage(sender, ChatColor.RED + "Incorrect syntax. Did you mean " + ChatColor.YELLOW + "/warp <name>" + ChatColor.RED + "?");
                    } else {
                        String name = args[0].toLowerCase();
                        if (manager.hasWarp(name)) {
                            Warp warp = manager.getWarp(name);
                            Location l = warp.toBukkit();
                            if (l.getWorld() == null) {
                                sendMessage(sender, ChatColor.RED + "Warp contains invalid location.");
                            } else {
                                sendMessage(sender, ChatColor.GRAY + "Whoosh!");
                                player.teleport(l);
                            }
                        } else {
                            sendMessage(sender, ChatColor.RED + "Warp '" + name + "' not found.");
                        }
                    }
                } else {
                    sendMessage(sender, ChatColor.RED + "No.");
                }
            }
        } else if (command.getName().equalsIgnoreCase("warps")) {
            if (!sender.hasPermission("DumbWarp.list")) {
                sendMessage(sender, ChatColor.RED + "No permission.");
            } else {
                List<Warp> warps = manager.getWarps();
                if (warps.size() <= 0) {
                    sendMessage(sender, ChatColor.RED + "No warps!");
                    return true;
                }
                int startAt = 0;
                if (args.length > 0) {
                    try {
                        int i = Integer.parseInt(args[0]) - 1;
                        if (i < 0) {
                            i = 0;
                        }
                        startAt = i * 8;
                    } catch (NumberFormatException e) {
                        sendMessage(sender, ChatColor.RED + "Incorrect syntax. Try " + ChatColor.YELLOW + "/warps <page number>" + ChatColor.RED + ".");
                        return true;
                    }
                }
                int pages = (int) Math.ceil(warps.size() / 8.0);
                int page = (int) (startAt / 8.0) + 1;
                if (page > pages) {
                    sendMessage(sender, ChatColor.RED + "Page out of range.");
                } else {
                    sendMessage(sender, ChatColor.DARK_GREEN + "====[ " + ChatColor.GREEN + "Known Warps " + ChatColor.DARK_GREEN + "|" + ChatColor.GREEN + " Page " + page + "/" + pages + ChatColor.DARK_GREEN + " ]====");
                    for (int i = startAt; i < (startAt + 8); i++) {
                        if (i < warps.size()) {
                            Warp warp = warps.get(i);
                            sendMessage(sender, ChatColor.AQUA + warp.getName() + " " + ChatColor.GRAY + "(" + warp.getWorld() + ", " + round(warp.getX(), 2) + ", " + round(warp.getY(), 2) + ", " + round(warp.getZ(), 2) + ")");
                        } else {
                            break;
                        }
                    }
                    sendMessage(sender, ChatColor.DARK_GREEN + "====[ " + ChatColor.GREEN + "Known Warps " + ChatColor.DARK_GREEN + "|" + ChatColor.GREEN + " Page " + page + "/" + pages + ChatColor.DARK_GREEN + " ]====");
                }
            }
        } else {
            sendMessage(sender, ChatColor.RED + "Something broke.");
        }
        return true;
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.GRAY + "[DumbWarp] " + ChatColor.WHITE + message);
    }

    private double round(double v, int n) {
        return new BigDecimal(v).setScale(n, RoundingMode.HALF_EVEN).doubleValue();
    }

}
