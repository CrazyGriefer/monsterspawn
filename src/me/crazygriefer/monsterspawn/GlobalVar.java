package me.crazygriefer.monsterspawn;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

// These variables can be accessed in any class by using GlobalVar.<variable> (e.g. GlobalVar.Interval)
public class GlobalVar {
	public static Integer Interval = 50;
	public static String Prefix = ChatColor.GOLD + "[" + ChatColor.AQUA + "MonsterSpawn" + ChatColor.GOLD + "] " + ChatColor.YELLOW;
	public static Integer SpawnerOn = 0;
	public static Integer Counter = 0;
	public static Integer TotalMobs = 0;
	public static String SelectedMob = "wither";
	public static java.util.List<String> AllMobs = Arrays.asList("bat", "bee", "blaze", "cat", "cat", "cave_spider", "chicken", "cod", "cow", "creeper", "dolphin", "donkey", "drowned", "elder_guardian", "ender_dragon", "enderman", "endermite", "evoker", "fox", "ghast", "giant", "guardian", "horse", "hoglin", "husk", "illusioner", "iron_golem", "llama", "magma_cube", "mooshroom", "mule", "ocelot", "panda", "parrot", "phantom", "pig", "piglin", "pillager", "polar_bear", "pufferfish", "rabbit", "ravager", "salmon", "sheep", "shulker", "silverfish", "skeleton", "skeleton_horse", "slime", "snow_golem", "spider", "squid", "stray", "strider", "turtle", "vex", "villager", "vindicator", "wandering_trader", "witch", "wither", "wither_skeleton", "wolf", "zoglin", "zombie", "zombie_horse", "zombie_villager", "zombified_piglin");
	public static String[] LoadingArray = {"+", "x"};
	public static String mobToSpawn = "";
	public static String MobName = "";
	public static String MobTitle = "Jerry";
	public static String Temp1 = "";
	public static String Host = "";
	public static Boolean HostOnly = false;
	public static Integer LoadingWidth = 50;
	public static String MessageTitle = "";
	public static String MessageSubtitle = "";
}
