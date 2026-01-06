package gg.ninjagaming.ninjafreebuild.commands.wilderness

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import gg.ninjagaming.ninjafreebuild.managers.WorldManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class WildernessCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cOnly players can use this command!")
            return true
        }

        if (sender.world.name == "wilderness") {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou can't use this command from the wilderness!")
            return true
        }

        val config = NinjaFreebuild.getConfig()

        val rtpAreaString = config.getString("world_configuration.wilderness.rtp_area")

        var rtpArea = rtpAreaString?.toDouble()

        if (rtpArea == null)
            rtpArea = 1000.0

        var isLocationValid = false
        var retries = 0
        val maxRetries = config.getString("world_configuration.max_rtp_attempts")?.toInt()?: 99

        var randomX = 0.0
        var randomZ = 0.0
        var randomY = 0.0

        val wildernessWorld = Bukkit.getWorld("wilderness")

        while (!isLocationValid)
        {
            retries++

            if (retries == maxRetries) {
                sender.sendMessage("${NinjaFreebuild.getPrefix()}§cUnable to find a valid random location in the Wilderness World for you. " +
                        "This Operation Exceeded the maximum Amount of $maxRetries attempts, please try again later")
                return true
            }

            randomX =-rtpArea + Random.nextDouble() * (rtpArea - -rtpArea)
            randomZ =-rtpArea + Random.nextDouble() * (rtpArea - -rtpArea)

            if (wildernessWorld == null) {
                sender.sendMessage("${NinjaFreebuild.getPrefix()}§cThe wilderness world is not loaded, please try again later")

                return true
            }

            randomY = wildernessWorld.getHighestBlockAt(Location(wildernessWorld, randomX, 0.0, randomZ)).location.y

            isLocationValid = WorldManager.isRandomTeleportLocationValid(wildernessWorld,Location(wildernessWorld,randomX,randomY,randomZ))
        }

        sender.sendMessage("${NinjaFreebuild.getPrefix()}§aTeleporting you to the wilderness...")

        sender.teleport(Location(wildernessWorld, randomX, randomY, randomZ))
        sender.sendMessage("${NinjaFreebuild.getPrefix()}§aFinding your new Location took $retries attempts")

     return true
    }
}