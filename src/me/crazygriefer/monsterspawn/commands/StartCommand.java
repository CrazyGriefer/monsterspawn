package me.crazygriefer.monsterspawn.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.crazygriefer.monsterspawn.GlobalVar;
import me.crazygriefer.monsterspawn.Main;
import net.md_5.bungee.api.ChatColor;

public class StartCommand implements CommandExecutor {

	private Main plugin;
	
	public StartCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("msstart").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(GlobalVar.Prefix + ChatColor.RED + "Only players may execute this command!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("msstart")) {
			Player p = (Player) sender;
			
			if (p.hasPermission("monsterspawn.start")) {
				GlobalVar.MobTitle = plugin.getConfig().getString("Mob.MobTitle");
				GlobalVar.SelectedMob = plugin.getConfig().getString("Mob.SelectedMob");
				if (GlobalVar.SpawnerOn == 0) {
					// Enable the spawning of mobs.
					GlobalVar.SpawnerOn = 1;
					GlobalVar.Counter = GlobalVar.Interval - 1;
					
					// Set the TotalMob counter to 0.
					GlobalVar.TotalMobs = 0;
				
					// Broadcast the message to the console.
					if (GlobalVar.SelectedMob.contains("random")) {
						GlobalVar.MobName = "random mob";
					} else {
						GlobalVar.MobName = GlobalVar.SelectedMob;
					}
					Integer Interval_sec = GlobalVar.Interval / 10;
					Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + "MonsterSpawn has started! A " + GlobalVar.MobName + " will now spawn every " + Interval_sec + " seconds!" + ChatColor.WHITE + " *scary noises*");
					
					// Notify the player who ran the command that MonsterSpawn is active.
					p.sendMessage(GlobalVar.Prefix + "MonsterSpawn has started! A " + GlobalVar.MobName + " will now spawn every " + Interval_sec + " seconds!");
					
					// Set the player who ran the command as the Host.
					GlobalVar.Host = p.getName();
					
					
					if (GlobalVar.HostOnly == true) {
						// Only tell the host what is about to happen.
						p.sendTitle(ChatColor.RED + GlobalVar.MessageTitle, ChatColor.AQUA + GlobalVar.MessageSubtitle, 5, 70, 5);
						p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
					} else {
						// Tell each player what is about to happen.
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.sendTitle(ChatColor.RED + GlobalVar.MessageTitle, ChatColor.AQUA + GlobalVar.MessageSubtitle, 5, 70, 5);
							player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
					}
						
					}
				} else {
					// Tell the player that MonsterSpawner is already active.
					p.sendMessage(GlobalVar.Prefix + "MonsterSpawner has already started! Use the command " + ChatColor.RED + "/msstop" + ChatColor.YELLOW + " to stop it.");
				}
			} else {
				// Tell the player who ran the command that they have no permission to run it.
				p.sendMessage(GlobalVar.Prefix + ChatColor.RED + "You do not have permission to execute this command!");
			}
			
		}
		
		return false;
	}
	
}
