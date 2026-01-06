package gg.ninjagaming.ninjafreebuild.commands.spawn

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import gg.ninjagaming.ninjafreebuild.managers.HomeManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.time.ZoneOffset

class SpawnCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cOnly players can use this command!")
            return true
        }

        val config = NinjaFreebuild.getConfig()

        val spawnWorldName = config.getString("world_configuration.spawn.world_name")

        if (spawnWorldName == null) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cThe spawn world is not configured, please contact an administrator")
            return true
        }

        val spawnWorld = Bukkit.getWorld(spawnWorldName)

        if (spawnWorld == null) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cThe spawn world is not loaded, please try again later")
            return true
        }

        val spawnX = config.getDouble("world_configuration.spawn.spawn_location_x")
        val spawnY = config.getDouble("world_configuration.spawn.spawn_location_y")
        val spawnZ = config.getDouble("world_configuration.spawn.spawn_location_z")
        val spawnYaw = config.getDouble("world_configuration.spawn.spawn_yaw")
        val spawnPitch = config.getDouble("world_configuration.spawn.spawn_pitch")

        if (!HomeManager.hasHomeSet(sender) && sender.world.name != spawnWorldName) {

            val playerLocation = sender.location
            sender.sendMessage("${NinjaFreebuild.getPrefix()}You haven't set a home yet! (/home set) Please do so before teleporting to spawn!")

            if(sender.firstPlayed > LocalDateTime.now().minusWeeks(1).toEpochSecond(ZoneOffset.UTC)) {
                sender.sendMessage("${NinjaFreebuild.getPrefix()}Because you are new on the server we captured your last location: " +
                        "x: ${playerLocation.x} y: ${playerLocation.y} z: ${playerLocation.z}!")

            }

        }

        sender.sendMessage("${NinjaFreebuild.getPrefix()}§aTeleporting you back to spawn...")
        sender.teleport(Location(spawnWorld, spawnX, spawnY, spawnZ, spawnYaw.toFloat(), spawnPitch.toFloat()))



        return true
    }


}