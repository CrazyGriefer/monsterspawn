# monsterspawn
A random monster spawner plugin for Minecraft 1.16

## About
Monsterspawn is a plugin that will spawn a monster every 30 (default) seconds. You can configure which mob you want to spawn. It can even spawn a random mob if you specify random as the mobtype. More about this later.

If you do decide to create a video using this plugin, please consider a link to this repository. This is not required, but it would be very appreciated.

## Commands
**/msstart:** This will start the spawning of monsters.
**/msstop:** This will stop the plugin from spawning mobs. It will also kill previously spawned entities.
**/msmob \<entity> \<name>:** Configure the mobtype + name of the mob you want to spawn. (See examples)
**/msinterval** Specify the amount of time (in seconds) between each spawn.

## Examples
Now let me show you some example commands.

### /msmob:
**/msmob random Subscribe to me!** will configure the plugin so when MonsterSpawn is active it will spawn a random mob called Subscribe to me!.
**/msmob wither Jerry** will configure the plugin so it will spawn ONLY withers called Jerry.

💡 If you want to see what the current selected mob is, just run **/msmob** (so with no arguments). It will tell you the mob and the name of the mob.

### /msstart:
**/msstart** will start all the chaos.

### /msstop:
**/msstop** will stop spawning monsters (and kill the previously spawned ones).

### /msinterval:
**/msinterval 30** will set the interval between spawns to 30 seconds. Every 30 seconds a new monster will spawn.

🔧You can NOT change the interval time while the plugin is running!

💡If you want to see what the current interval is, just run **/msinterval** (so with no arguments). This will tell you want the current interval is.

## Permissions
All the permissions are named after their command:
* /msstart    - monsterspawn.start
* /msstop     - monsterspawn.stop
* /msinterval - monsterspawn.interval
* /msmob      - monsterspawn.mob

## Little note
* This is my first plugin. I made this one after a friend of me posted a video spawning a wither every 30 seconds, but it was done using commandblocks. I am sure there is a lot of place for improvement, and I am thankful to anybody who is willing to contribute to the project.
* This project took about 6 days in total.
* If you still have questions or have a suggestion, please join our [Discord Server](https://discord.gg/DfwrVBf6)
