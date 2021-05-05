package me.crazygriefer.monsterspawn;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent event) {
		
		// If MonsterSpawn is enabled.
		if (GlobalVar.SpawnerOn == 1) {
			
			// If DisableSpector is disabled.
			if (!GlobalVar.DisableSpectator) {
		
				// Get the player object
				Player p = event.getEntity();
		
				// Heal the player.
				p.setHealth(20);
				
				// Get the players location.
				Location loc = p.getLocation();
				
				// Keep lowering the y-coord until it will hit the ground.
				while (loc.getBlock().getType() == Material.AIR || (loc.getBlock().getType() == Material.GRASS) || (loc.getBlock().getType() == Material.SNOW)) {
					loc.setY(loc.getY() - 1);
				} 
				
				// Increment the y-coord by one (otherwise the 'body' will be IN the ground).
				loc.setY(loc.getY() + 1);
				
				// Set a fence at the player's location.
				p.getWorld().getBlockAt(loc).setType(Material.ACACIA_FENCE);
				
				// Define the skull location.
				loc.setY(loc.getY() + 1);
				
				// Get the block.
				Block skullBlock = loc.getBlock();
				
				// Place down the block.
		        	skullBlock.setType(Material.PLAYER_HEAD);
		        
		        	// Get the current blockstate.
		        	BlockState state = skullBlock.getState();
		        
		        	// Define the blockstate as skull.
		        	Skull skull = (Skull) state;
		        
		        	// Get the UUID of the player that died.
		        	UUID uuid = p.getUniqueId();
		        
		        	// Set the head to the person's skin.
		        	skull.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(uuid));
		        
		        	// Update the head.
		        	skull.update();
		
				// Add the player to the array of players that died.
				GlobalVar.PlayersDied.add(p.getName());
		
				// Place the player in spectator mode.
				p.setGameMode(GameMode.SPECTATOR);
				
				// If custom death messages is enabled.
				if (GlobalVar.CustomMessagesEnabled) {
					
					// Get a random death message.
					Random rand = new Random();
			        String message = GlobalVar.DeathMessages.get(rand.nextInt(GlobalVar.DeathMessages.size()));

			        // Replace %player% with the player's displayname.
			        message = replaceholder(message, "player", p.getDisplayName());
			        
			        	// Send the custom death message.
					event.setDeathMessage(message);
				}
			}
		}
	}
	
	// Replaceholder function. GET IT?! Replace + Placeholder = Replaceholder. xxxD
	// Anyway, I stole SOME (trust me) of it from StackOverflow, but also did a lot myself. 
	private String replaceholder(String text, String placeholder, String replaceto) {
		
		// Create a string buffer.
    		StringBuffer sb = new StringBuffer();
    	
    		// Configure the matcher to look for the selected placeholder in the given text string.
    		Matcher m = Pattern.compile("\\%(" + placeholder + ")\\%").matcher(text);
    	
    		// When the pattern is found.
    		while (m.find()) {
    		
			// Replace it to the given replaceto input.
        		m.appendReplacement(sb, "" + replaceto);
    		}
    	
    		// Append the string to the stringbuilder.
    		m.appendTail(sb);
    	
    		// Return the string with the replaced placeholders.
		return sb.toString();
	}
}
