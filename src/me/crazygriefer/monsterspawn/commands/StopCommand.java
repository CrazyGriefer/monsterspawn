package me.crazygriefer.monsterspawn.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.crazygriefer.monsterspawn.GlobalVar;
import me.crazygriefer.monsterspawn.Main;
import net.md_5.bungee.api.ChatColor;

public class StopCommand implements CommandExecutor {

	@SuppressWarnings("unused")
	private Main plugin;
	
	public StopCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("msstop").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(GlobalVar.Prefix + ChatColor.RED + "Only players may execute this command!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("msstop")) {
			Player p = (Player) sender;
			
			if (p.hasPermission("monsterspawn.stop")) {
				if (GlobalVar.SpawnerOn == 1) {
					// Enable the spawning of mobs...
					GlobalVar.SpawnerOn = 0;
				
					// Broadcast the message to the console
					Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + "MonsterSpawn has stopped. Stay safe my friend!" + ChatColor.WHITE + " *cricket sounds*");
					
					// Kill the wither + the netherstar it drops.
					ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
					Bukkit.dispatchCommand(console, "execute at @a run kill @e[tag=monsterspawn]");
					Bukkit.dispatchCommand(console, "execute at @a run kill @e[type=item,nbt={Item:{id:\"minecraft:nether_star\",Count:1b}}]");
					
					// Tell all players that MonsterSpawn has stopped...
					for (Player player : Bukkit.getOnlinePlayers()) {
						player.sendMessage(GlobalVar.Prefix + "MonsterSpawn has stopped. Stay safe my friend!");
					}
				} else {
					p.sendMessage(GlobalVar.Prefix + "MonsterSpawner has not been started yet. Use the command " + ChatColor.RED + "/msstart" + ChatColor.YELLOW + " to start it.");
				}
			} else {
				p.sendMessage(GlobalVar.Prefix + ChatColor.RED + "You do not have permission to execute this command!");
			}
			
		}
		
		return false;
	}
	
}
