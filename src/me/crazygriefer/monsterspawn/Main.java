package me.crazygriefer.monsterspawn;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.crazygriefer.monsterspawn.commands.*;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {

	
	
	public void onEnable() {
		// Starting MonsterSpawn.
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.BLUE + "Starting MonsterSpawn");
		
		// Importing the config file.
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.BLUE + "Importing the config file");
		FileConfiguration config = this.getConfig();
		
		// Setting up default config file.
		config.addDefault("Spawning.Interval", 30);
		config.addDefault("Spawning.X-offset", 0);
		config.addDefault("Spawning.Y-offset", 0);
		config.addDefault("Spawning.Z-offset", 0);
		config.addDefault("Spawning.Interval", 30);
		
		config.addDefault("Mob.MobTitle", "Jerry");
		config.addDefault("Mob.SelectedMob", "random");
		
		config.addDefault("Message.Title", "☠ The end is near ☠");
		config.addDefault("Message.Subtitle", "How long can  you survive?");
		
		// Saving the default config file.
		config.options().copyDefaults(true);
		saveConfig();
		
		// Getting the MobTitle and SelectedMob values from the config file.
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.BLUE + "Setting variables");
		GlobalVar.MobTitle = getConfig().getString("Mob.MobTitle");
		GlobalVar.SelectedMob = getConfig().getString("Mob.SelectedMob");
		
		GlobalVar.MessageTitle = getConfig().getString("Message.Title");
		GlobalVar.MessageSubtitle = getConfig().getString("Message.Subtitle");
		
		GlobalVar.HostOnly = getConfig().getBoolean("Spawning.HostOnly");
		GlobalVar.Interval = getConfig().getInt("Spawning.Interval") * 10;
		Integer x_offset = getConfig().getInt("Spawning.X-offset");
		Integer y_offset = getConfig().getInt("Spawning.Y-offset");
		Integer z_offset = getConfig().getInt("Spawning.Z-offset");
		
		// Enabling commands.
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.BLUE + "Enabling commands");
		new StartCommand(this);
		new StopCommand(this);
		new SelectCommand(this);
		new IntervalCommand(this);
		
		// Configuring filters.
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.BLUE + "Configuring filters");
		getLogger().setFilter(record -> !record.getMessage().toLowerCase().contains("Showing new actionbar title"));
		
		// Stating that the plugin has started
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.GREEN + "MonsterSpawn has successfully been enabled!");
		
		// Providing very useful information.
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.BLUE + "This plugin is free. If you paid for it you have been swindled!");
		
		{	
			// Create a new repeating task (Scheduler).
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				int loadingindex = 0;
				@Override
				public void run() { 
					// Only execute when MonsterSpawner is activated.
					if (GlobalVar.SpawnerOn == 1) {
						// When the 30 seconds are over and a new mob has to spawn:
						if ((int) GlobalVar.Counter == (int) GlobalVar.Interval) {
							
							// Set the counter to 0 again.
							GlobalVar.Counter = 0;

							// Set the list variable.

							
							// Check if HostOnly is enabled. If so, only summon at the host.. Else, set @a as the target.
							if (GlobalVar.HostOnly) {
								
								// Check if the mobtype is set to random. If so, pick a random mob. Else, pick the mob that the user defined.
								if (GlobalVar.SelectedMob.contains("random")) {
									Random rand = new Random();
								    GlobalVar.mobToSpawn = GlobalVar.AllMobs.get(rand.nextInt(GlobalVar.AllMobs.size()));
								} else {
									GlobalVar.mobToSpawn = GlobalVar.SelectedMob;
								}
								
								ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
								Player player = getServer().getPlayer(GlobalVar.Host);
								Bukkit.dispatchCommand(console, "execute at " + GlobalVar.Host + " run summon " + GlobalVar.mobToSpawn +  " ~" + x_offset + " ~" + y_offset + " ~" + z_offset + " {Glowing:1b,CustomNameVisible:1b,DeathLootTable:\"minecraft:empty\",Tags:[\"monsterspawn\"],CustomName:'{\"text\":\"" + GlobalVar.MobTitle + "\",\"color\":\"blue\",\"bold\":true}'}");
								player.sendMessage(GlobalVar.Prefix + "A new " + ChatColor.BLUE + GlobalVar.mobToSpawn + ChatColor.YELLOW + " spawned!");
								GlobalVar.TotalMobs += 1;
							} else {
								
								// Spawn a mob at every player (using console).
								for (Player player : Bukkit.getOnlinePlayers()) {
									
									// Check if the mobtype is set to random. If so, pick a random mob. Else, pick the mob that the user defined.
									if (GlobalVar.SelectedMob.contains("random")) {
										Random rand = new Random();
									    GlobalVar.mobToSpawn = GlobalVar.AllMobs.get(rand.nextInt(GlobalVar.AllMobs.size()));
									} else {
										GlobalVar.mobToSpawn = GlobalVar.SelectedMob;
									}
									
									// Execute the command.
									ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
									Bukkit.dispatchCommand(console, "execute at " + player.getName() + " run summon " + GlobalVar.mobToSpawn +  " ~" + x_offset + " ~" + y_offset + " ~" + z_offset + " {Glowing:1b,CustomNameVisible:1b,DeathLootTable:\"minecraft:empty\",Tags:[\"monsterspawn\"],CustomName:'{\"text\":\"" + GlobalVar.MobTitle + "\",\"color\":\"blue\",\"bold\":true}'}");
									player.sendMessage(GlobalVar.Prefix + "A new " + ChatColor.BLUE + GlobalVar.mobToSpawn + ChatColor.YELLOW + " spawned!");
									
									// Increment the total amount of mobs.
									GlobalVar.TotalMobs += 1;
								}
							}
							
							// Notify players that a new mob has spawned (and how many in total there are now).
							// This was before the 'host-only' thing was an option, commented it out just to be sure ;)
							// Bukkit.broadcastMessage(GlobalVar.Prefix + "A new " + GlobalVar.mobToSpawn + " spawned! (Total: " + ChatColor.RED + GlobalVar.TotalMobs + ChatColor.YELLOW + ")");
						}
						
						// Define variables.
						String ActionBarText = "[\"\",{\"text\":\"New monster spawn:\",\"color\":\"yellow\"},{\"text\":\" ";
						int loadingwidth = GlobalVar.LoadingWidth;
						float interval = (float) GlobalVar.Interval;
						
						// Caculate how many '+' characters there should be in the Actionbar.
				        int loading = Math.round(GlobalVar.Counter / interval * (float) loadingwidth);
				        
				        // Calculate how many percent there should be displayed.
				        int percent = (int) Math.round((GlobalVar.Counter / interval * 100));
				        
				        // Get the right amount of '+' characters for the Actionbar.
				        for (int i = loading -1; i > 1; i--) {
				        	ActionBarText += "+";
				        }
				        
				        // Add the right spinning symbol from the array.
				        ActionBarText += GlobalVar.LoadingArray[loadingindex];
				        loadingindex = loadingindex + 1;
				        
				        // If the end of the spinning symobl list has been spotted, start again at the end.
				        if (loadingindex == GlobalVar.LoadingArray.length) {
				        	loadingindex = 0;
				        }
				        
				        // Close the text and add the text for the rest of the Actionbar.
				        ActionBarText += "\",\"color\":\"red\"},{\"text\":\"";
				        
				        // Get the right amount of '-' characters for the Actionbar.
				        for (int i = loadingwidth + 1 - loading; i > 1; i--) {
				        	ActionBarText += "-";
				        }
				        
				        // Finish up the Actionbar with the right percentage.
				        ActionBarText += "\",\"color\":\"green\"},{\"text\":\" " + percent + "%\",\"color\":\"yellow\"}]";
				        
				        // Check if the loadingwidth is set to 0. If so, do NOT display the actionbar.
				        if (loadingwidth != 0) {
				        	
				        	// When everything is prepared, run the command to show the Actionbar to the players.
				        	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
				        	Bukkit.dispatchCommand(console, "title @a actionbar " + ActionBarText);
				        }
					}
					
					// Increment the Counter variable.
					GlobalVar.Counter += 1;
				}
			}, 0L, 2L); //0 Tick initial delay, 2 Tick (0.1 Second) between repeats.
		}
	}
}

