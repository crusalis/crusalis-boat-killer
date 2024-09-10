package org.oreo.boatItemKiller.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Boat
import org.bukkit.entity.Player
import org.oreo.boatItemKiller.BoatItemKiller

class BoatDeleteCommand(private val plugin: BoatItemKiller) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player || !sender.isOp) {
            sender.sendMessage("§cYou must be an OP player to use this command.")
            return true
        }

        if (args.isNullOrEmpty()) {
            sender.sendMessage("§cUsage: /$label <enable|disable>")
            return true
        }

        when (args[0].lowercase()) {
            "enable" -> {
                plugin.boatKillEnabled = true
                sender.sendMessage("${ChatColor.AQUA}Boat deletion enabled!")

                deleteActiveBoats(sender)
            }
            "disable" -> {
                plugin.boatKillEnabled = false
                sender.sendMessage("${ChatColor.AQUA}Boat deletion disabled!")
            }
            else -> {
                sender.sendMessage("§cInvalid subcommand. Use either 'enable' or 'disable'.")
            }
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        if (args.size == 1) {
            return listOf("enable", "disable").filter { it.startsWith(args[0].lowercase()) }.toMutableList()
        }
        return null
    }


    private fun deleteActiveBoats(player: Player){
        val world = player.world

        val allBoats = world.getEntitiesByClasses(Boat::class.java)

        for (boat in allBoats){

            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                boat.remove()
            }, plugin.boatDeleteDelay * 20L)

        }
    }
}
