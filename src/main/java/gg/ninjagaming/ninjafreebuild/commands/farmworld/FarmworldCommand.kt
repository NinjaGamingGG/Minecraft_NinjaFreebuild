package gg.ninjagaming.ninjafreebuild.commands.farmworld

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import gg.ninjagaming.ninjafreebuild.database.tables.FarmWorldIndex
import gg.ninjagaming.ninjafreebuild.database.tables.LastPlayerWorldPosition
import gg.ninjagaming.ninjafreebuild.managers.WorldManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.ktorm.database.iterator
import org.ktorm.dsl.*
import java.time.Instant
import java.util.*
import kotlin.random.Random

class FarmWorldCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cOnly players can use this command!")
            return false
        }

        val database = NinjaFreebuild.getDatabase()

        if (database == null){
            println("${NinjaFreebuild.getPrefix()}§cError: Database is not connected!")
            return false
        }

        val config = NinjaFreebuild.getConfig()

        val farmWorlds = database.from(FarmWorldIndex).select()

        var farmWorldCreated: Instant ?= null
        var farmWorldId= ""

        for (row in farmWorlds.rowSet)
        {
            farmWorldId = row[FarmWorldIndex.FarmWorldId] as String
            farmWorldCreated = row[FarmWorldIndex.CreatedAt] as Instant
        }


        if (farmWorldId == "")
            return false

        val worldName = "Farmworld-$farmWorldId"

        val lastPosition = database.from(LastPlayerWorldPosition)
            .select()
            .where((LastPlayerWorldPosition.PlayerId eq sender.uniqueId.toString()) and (LastPlayerWorldPosition.WorldName eq worldName))

        var lastPositionX = 0.0
        var lastPositionY = 0.0
        var lastPositionZ = 0.0
        var lastPositionPitch = 0f
        var lastPositionYaw = 0f


        val farmWorld = Bukkit.getWorld(worldName)

        if (lastPosition.rowSet.size() == 0)
        {
            val spawnRadius = config.getString("world_configuration.farmworld.spawn_radius")?.toInt()?: 200

            var isLocationValid = false
            var retries = 0
            val maxRetries = config.getString("world_configuration.max_rtp_attempts")?.toInt()?: 99

            while (!isLocationValid) {
                retries++

                if (retries == maxRetries)
                    return true

                lastPositionX =-spawnRadius + Random.nextDouble() * (spawnRadius - -spawnRadius)
                lastPositionZ =-spawnRadius + Random.nextDouble() * (spawnRadius - -spawnRadius)

                if (farmWorld == null)
                    return false

                lastPositionY = farmWorld.getHighestBlockYAt(Location(farmWorld,lastPositionX,0.0,lastPositionZ)).toDouble()

                isLocationValid = WorldManager.isRandomTeleportLocationValid(farmWorld,
                    Location(farmWorld,lastPositionX,lastPositionY,lastPositionZ)
                )
            }


        }

        for (row in lastPosition.rowSet)
        {
            lastPositionX = row[LastPlayerWorldPosition.WorldLocationX] as Double
            lastPositionY = row[LastPlayerWorldPosition.WorldLocationY] as Double
            lastPositionZ = row[LastPlayerWorldPosition.WorldLocationZ] as Double
            lastPositionPitch = row[LastPlayerWorldPosition.WorldLocationPitch] as Float
            lastPositionYaw = row[LastPlayerWorldPosition.WorldLocationYaw] as Float
        }

        sender.teleport(Location(Bukkit.getWorld(worldName),lastPositionX,lastPositionY,lastPositionZ,lastPositionYaw,lastPositionPitch))

        sender.sendMessage("${NinjaFreebuild.getPrefix()}You are now in the Farmworld! You are allowed to Remove/ Build Blocks wherever you wish! §cThis world gets reset after a few days")

        val nextReset = farmWorldCreated!!.plusSeconds(60*config.getLong("world_configuration.farmworld.life_time_minutes")).minusSeconds(
            Calendar.getInstance().time.toInstant().epochSecond).epochSecond/60

        if (nextReset <= 720)
        {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cThis world is going to be reset soon!")
            return true
        }
        sender.sendMessage("${NinjaFreebuild.getPrefix()}Next Reset in: $nextReset minutes")


        return true
    }

}