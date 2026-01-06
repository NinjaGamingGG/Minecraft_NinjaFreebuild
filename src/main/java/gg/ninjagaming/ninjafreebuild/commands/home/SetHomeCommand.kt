package gg.ninjagaming.ninjafreebuild.commands.home

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import gg.ninjagaming.ninjafreebuild.managers.HomeManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SetHomeCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (args.isEmpty())  {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou didnt specify a home name, setting your default home")
            return HomeManager.setHome(sender, "default")
        }

        return HomeManager.setHome(sender, args[0])
    }
}