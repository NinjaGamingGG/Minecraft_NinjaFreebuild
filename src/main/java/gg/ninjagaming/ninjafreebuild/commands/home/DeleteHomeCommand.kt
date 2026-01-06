package gg.ninjagaming.ninjafreebuild.commands.home

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import gg.ninjagaming.ninjafreebuild.managers.HomeManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class DeleteHomeCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (args.isEmpty())  {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou didnt specify a home name, please do so to continue")
            return false
        }
        return HomeManager.deleteHome(sender, args[0])
    }
}