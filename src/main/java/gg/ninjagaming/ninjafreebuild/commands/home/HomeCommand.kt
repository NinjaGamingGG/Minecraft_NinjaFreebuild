package gg.ninjagaming.ninjafreebuild.commands.home

import gg.ninjagaming.ninjafreebuild.managers.HomeManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
class HomeCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (args.isEmpty()) {
            return HomeManager.teleportHome(sender, "default")
        }


        when (args[0].lowercase()) {
            "set" -> {
                if (args.size == 1)
                    return HomeManager.setHome(sender, "default")

                return HomeManager.setHome(sender, args[1])
            }
            "delete" -> {
                if (args.size == 1)
                    return false

                return HomeManager.deleteHome(sender, args[1])
            }
            "list" -> {
                return HomeManager.listHomes(sender)
            }
            else -> {

                return HomeManager.teleportHome(sender, args[0])
            }
        }
    }
}