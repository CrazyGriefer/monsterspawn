package me.crazygriefer.monsterspawn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.crazygriefer.monsterspawn.commands.IntervalCommand;
import me.crazygriefer.monsterspawn.commands.SelectCommand;
import me.crazygriefer.monsterspawn.commands.StartCommand;
import me.crazygriefer.monsterspawn.commands.StopCommand;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	public void onEnable() {
		// Starting MonsterSpawn.
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.BLUE + "Starting MonsterSpawn");
		
		// Importing the config file.
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.BLUE + "Importing the config file");
		FileConfiguration config = this.getConfig();
		
		// Setting up default config file.
		config.addDefault("FirstRun", true);
		config.addDefault("Spawning.Interval", 30);
		config.addDefault("Spawning.X-offset", 0);
		config.addDefault("Spawning.Y-offset", 0);
		config.addDefault("Spawning.Z-offset", 0);
		config.addDefault("Spawning.Interval", 60);
		config.addDefault("Spawning.HostOnly", false);
		
		config.addDefault("Mob.MobTitle", "Jerry");
		config.addDefault("Mob.SelectedMob", "random");
		
		config.addDefault("Message.Title", "☠ The end is near ☠");
		config.addDefault("Message.Subtitle", "How long can  you survive?");
		
		config.addDefault("Death.DisableSpectator", false);
		config.addDefault("Death.EnableCustomMessages", true);
		
		List<String> DeathMessages = new ArrayList<String>();
		DeathMessages.add("%player% could not stand the mobs!");
		DeathMessages.add("What was that? Oh, it was %player% dying!");
		config.addDefault("Death.Messages", DeathMessages);
		
		
		
		
		// Saving the default config file.
		config.options().copyDefaults(true);
		saveConfig();
		
		getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
		
		// If this is the first time the plugin is running.
		if(config.getBoolean("FirstRun")) { 
			
			// Set broadcast-console-to-ops to false in server.properties.
			File serverproperties = new File("server.properties");
			setString("broadcast-console-to-ops", serverproperties, "false");
			
			// Set FirstRun to false in config.yml
			config.set("FirstRun", false);
			saveConfig();
			
			// Warn the player that this is the first time run.
			Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.RED + "Warning: This is the first time MonsterSpawn is running on this server. ");
			Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.RED + "MonsterSpawn is done setting itself up. However, you need to restart or ");
			Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.RED + "reload your server in order for the changes to take effect!");
			
			// Set the global variable to true.
			GlobalVar.FirstRun = true;
		}		
		
		// Getting the MobTitle and SelectedMob values from the config file.
		Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.BLUE + "Setting variables");
		GlobalVar.MobTitle = getConfig().getString("Mob.MobTitle");
		GlobalVar.SelectedMob = getConfig().getString("Mob.SelectedMob");
		
		// Getting the title and subtitle from the config file.
		GlobalVar.MessageTitle = getConfig().getString("Message.Title");
		GlobalVar.MessageSubtitle = getConfig().getString("Message.Subtitle");
		
		GlobalVar.HostOnly = getConfig().getBoolean("Spawning.HostOnly");
		GlobalVar.Interval = getConfig().getInt("Spawning.Interval") * 10;
		Integer x_offset = getConfig().getInt("Spawning.X-offset");
		Integer y_offset = getConfig().getInt("Spawning.Y-offset");
		Integer z_offset = getConfig().getInt("Spawning.Z-offset");
		
		GlobalVar.DisableSpectator = getConfig().getBoolean("Death.DisableSpectator");
		GlobalVar.CustomMessagesEnabled = getConfig().getBoolean("Death.EnableCustomMessages");
		
		List<String> messages = getConfig().getStringList("Death.Messages");
		GlobalVar.DeathMessages = messages;
		
		// Check if the interval is less than 1 second. If so, default back to 30.
		if (GlobalVar.Interval < 10) {
			
			// Reset the variable.
			GlobalVar.Interval = 300;
			
			// Reset the config file.
			getConfig().set("Spawning.Interval", 30);
			saveConfig();
			
			// Tell the console what has happened.
			Bukkit.getConsoleSender().sendMessage(GlobalVar.Prefix + ChatColor.YELLOW + "Warning: The Interval setting in config.yml is invalid! Defaulting to 30 seconds.");
		}
		
		
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
						
						// Before doing anything else (like spawning monsters), check if a player died. If so, switch them to spectator.


						// When the X amount of seconds is over:
						if ((int) GlobalVar.Counter == (int) GlobalVar.Interval) {
							
							// Set the counter to 0 again.
							GlobalVar.Counter = 0;
							
							// Check if HostOnly is enabled. If so, only summon at the host.. Else, set @a as the target.
							if (GlobalVar.HostOnly) {
								
								// Check if the mobtype is set to random. If so, pick a random mob. Else, pick the mob that the user defined.
								if (GlobalVar.SelectedMob.contains("random")) {
									Random rand = new Random();
								    GlobalVar.mobToSpawn = GlobalVar.AllMobs.get(rand.nextInt(GlobalVar.AllMobs.size()));
								} else {
									GlobalVar.mobToSpawn = GlobalVar.SelectedMob;
								}
								
								// Set the host as the player to spawn the mob.
								Player player = getServer().getPlayer(GlobalVar.Host);
								
								// Spawn the mob (using console).
								ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
								Bukkit.dispatchCommand(console, "execute at " + GlobalVar.Host + " run summon " + GlobalVar.mobToSpawn + " ~" + x_offset + " ~" + y_offset + " ~" + z_offset + " {Glowing:1b,Tags:[\"monsterspawn\"],CustomName:'{\"text\":\"" + GlobalVar.MobTitle + "\"}',ArmorItems:[{},{},{},{id:\"minecraft:leather_helmet\",Count:1b}],ArmorDropChances:[0.085F,0.085F,0.085F,0.000F]}");
								
								// Tell the player that a mob has spawned.
								player.sendMessage(GlobalVar.Prefix + "A new " + ChatColor.BLUE + GlobalVar.mobToSpawn + ChatColor.YELLOW + " spawned!");
								
								// Increment the totalmobs variable.
								GlobalVar.TotalMobs++;
							
							} else {
								
								// Spawn a mob at every player (using console).
								for (Player player : Bukkit.getOnlinePlayers()) {
									
									// If the player is not in spectator mode.
									if (player.getGameMode() != GameMode.SPECTATOR) {
										
										// Check if the mobtype is set to random. If so, pick a random mob. Else, pick the mob that the user defined.
										if (GlobalVar.SelectedMob.contains("random")) {
											Random rand = new Random();
											GlobalVar.mobToSpawn = GlobalVar.AllMobs.get(rand.nextInt(GlobalVar.AllMobs.size()));
										} else {
											GlobalVar.mobToSpawn = GlobalVar.SelectedMob;
										}
									
										// Spawn the mob (using console).
										ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
										Bukkit.dispatchCommand(console, "execute at " + player.getName() + " run summon " + GlobalVar.mobToSpawn + " ~" + x_offset + " ~" + y_offset + " ~" + z_offset + " {Glowing:1b,Tags:[\"monsterspawn\"],CustomName:'{\"text\":\"" + GlobalVar.MobTitle + "\"}',ArmorItems:[{},{},{},{id:\"minecraft:leather_helmet\",Count:1b}],ArmorDropChances:[0.085F,0.085F,0.085F,0.000F]}");
									
										// Tell the player that a mob has spawned.
										player.sendMessage(GlobalVar.Prefix + "A new " + ChatColor.BLUE + GlobalVar.mobToSpawn + ChatColor.YELLOW + " spawned!");
										
										// Increment the totalmobs variable.
										GlobalVar.TotalMobs++;
									}
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

	private String setString(String stringtochange, File f, String changeto) {
		
		Properties pr = new Properties();
        try
        {
            FileInputStream in = new FileInputStream(f);
            pr.load(in);
            pr.setProperty(stringtochange,changeto);
            pr.store(new FileOutputStream(f), null);
        }
        catch (IOException e)
        {
        }
		return "";
		
	}
	
}

