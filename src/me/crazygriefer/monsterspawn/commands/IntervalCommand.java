package me.crazygriefer.monsterspawn.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.crazygriefer.monsterspawn.GlobalVar;
import me.crazygriefer.monsterspawn.Main;
import net.md_5.bungee.api.ChatColor;

public class IntervalCommand implements CommandExecutor {

	private Main plugin;
	
	public IntervalCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("msinterval").setExecutor(this);
	}
	
	public static Integer NewInterval;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(GlobalVar.Prefix + ChatColor.RED + "Only players may execute this command!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("msinterval")) {
			Player p = (Player) sender;
			
			if (p.hasPermission("monsterspawn.interval")) {
				String MessageBack = "";
				
				// If no arguments are given, display the current mob.
				if (args.length == 0) {
					Integer interval_sec = GlobalVar.Interval / 10;
					MessageBack = GlobalVar.Prefix + "The current spawning interval is " + ChatColor.GREEN + interval_sec + ChatColor.YELLOW + " seconds.";
					
				// Else (if the user defined a interval to be set)
				} else {
					// Make sure Monsterspawn is not running, or things will break.
					if (GlobalVar.SpawnerOn == 0) {
						Boolean NoError = false;
					
						// Try converting the string to an integer. 
						try {
							NewInterval = Integer.parseInt(args[0]) * 10;
							NoError = true;
						} catch (NumberFormatException e) {	}
					
						// If no error occured.
						if (NoError) {
						
							// Check if the number entered is below 1. If so, tell the player that is not allowed.
							if (NewInterval < 10) {
								sender.sendMessage(GlobalVar.Prefix + ChatColor.RED + "Please use a number that is at least 1.");
							} else {
							
							// Tell the player the time has been set.
							MessageBack = GlobalVar.Prefix + "You set the spawning interval to "+ ChatColor.GREEN + args[0] + ChatColor.YELLOW + " seconds. " + NewInterval;
						
							// Set the time.
							GlobalVar.Interval = NewInterval;
							plugin.getConfig().set("Spawning.Interval", GlobalVar.Interval / 10);
							plugin.saveConfig();
							}
						} else {
						
							// If the player didn't enter integers only (aka when NumberFormatException occurs), the player to enter a valid number.
							MessageBack = GlobalVar.Prefix + ChatColor.RED + "Please enter a valid number!";
						}
					} else {
						// Else (so if MonsterSpawner was indeed running).
						MessageBack = GlobalVar.Prefix + ChatColor.RED + "You can not use this command while MonsterSpawner is active. Use /msstop first.";
					}
				}
				
				// Send the player the feedback depending on the arguments they gave.
				p.sendMessage(MessageBack);
			} else {
				p.sendMessage(GlobalVar.Prefix + ChatColor.RED + "You do not have permission to execute this command!");
			}
			
		}
		
		return false;
	}
	
}
