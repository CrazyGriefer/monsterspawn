package me.crazygriefer.monsterspawn.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.crazygriefer.monsterspawn.GlobalVar;
import me.crazygriefer.monsterspawn.Main;
import net.md_5.bungee.api.ChatColor;

public class SelectCommand implements CommandExecutor {

	private Main plugin;
	
	public SelectCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("msmob").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(GlobalVar.Prefix + ChatColor.RED + "Only players may execute this command!");
			return true;
		}
		
		// If the player runs this command.
		if (cmd.getName().equalsIgnoreCase("msmob")) {
			Player p = (Player) sender;
			
			GlobalVar.MobTitle = plugin.getConfig().getString("Mob.MobTitle");
			GlobalVar.SelectedMob = plugin.getConfig().getString("Mob.SelectedMob");
				
			// If no arguments are given, display the current mob.
			if (args.length == 0) {
				GlobalVar.Temp1 = GlobalVar.Prefix + "The current mob is: " + ChatColor.GREEN + GlobalVar.SelectedMob;
				
			// Else...
			} else {
				
				// If the player has permission to set the mob.
				if (p.hasPermission("monsterspawn.mob." + args[0]) || p.hasPermission("monsterspawn.mob.*")) {
				
					// If there is at least 1 argument.
					// And it is a valid mobtype.
					if((GlobalVar.AllMobs).contains(args[0])){
						GlobalVar.Temp1 = GlobalVar.Prefix + "You selected the mob "+ ChatColor.GREEN + args[0];
				    
						// Set the selected mob to the first given argument by the player.
						GlobalVar.SelectedMob = args[0];
						plugin.getConfig().set("Mob.SelectedMob", GlobalVar.SelectedMob);
					} else {
				    	
						// If the given argument was not in the moblist, check if they entered 'random'.
						if (args[0].equalsIgnoreCase("random")) {
				    	
							// If 'random' was given, set it to the SelectedMob.
							GlobalVar.Temp1 = GlobalVar.Prefix + "You selected the mob "+ ChatColor.GREEN + args[0];
							GlobalVar.SelectedMob = "random";
							plugin.getConfig().set("Mob.SelectedMob", GlobalVar.SelectedMob);
						} else {
				    	
							// Tell the player they need to read the documentation and fix their crap!
							p.sendMessage(GlobalVar.Prefix + "The mobtype " + ChatColor.RED + args[0] + ChatColor.YELLOW + " is invalid!");
						}
					}
				} else {
					// Tell the player who ran the command that they have no permission to run it.
					p.sendMessage(GlobalVar.Prefix + ChatColor.RED + "You do not have permission to execute this command!");
					return false;
				}				
			}
				
			// If there are at least 2 arguments it means that the player entered a name.
			// The following code will connect each after the 2nd arg (the name) to one string, thus making names with spaces possible.
			if (args.length > 1) {
				StringBuilder sb = new StringBuilder(); //creates StringBuilder
				int argnr = 1;
				
				// Loop args.
			    for (String arg : args) { 
			       	
			    	// Exclude the 1st arg (the mobtype).
			    	if (argnr != 1) {
			       		
			    		// Appends arg to StringBuilder.
			    		sb.append(arg); 
			    		if (argnr != args.length) {
			       			
			    			// Appends space after each arg (except the last one).
			    			sb.append(" "); 
			    		}
			    	}
			       	
			    	// Increment the argnr value by 1.
			    	argnr += 1;
			    }
			       
			    // Convert the generated list to a string.
				String arg1 = sb.toString();
				        
				// Set the MobTitle to the second given argument.
				GlobalVar.MobTitle = arg1;
				plugin.getConfig().set("Mob.MobTitle", GlobalVar.MobTitle);
			}
				
			// Send the info back to the player.
			GlobalVar.Temp1 += ChatColor.YELLOW + " (Name: " + ChatColor.BLUE + GlobalVar.MobTitle + ChatColor.RESET + ChatColor.YELLOW + ")" ;
			p.sendMessage(GlobalVar.Temp1);
			GlobalVar.Temp1 = "";
				
			// Save the changes made to the config.
			plugin.saveConfig();
		}
		return false;
	}
}
