package gg.ninjagaming.ninjafreebuild.commands.home

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import gg.ninjagaming.ninjafreebuild.managers.HomeManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class TeleportHomeCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if(args.isEmpty()) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou didnt specify a home name, teleporting to your default home")
            return HomeManager.teleportHome(sender, "default")
        }

        return HomeManager.teleportHome(sender, args[0])
    }
}