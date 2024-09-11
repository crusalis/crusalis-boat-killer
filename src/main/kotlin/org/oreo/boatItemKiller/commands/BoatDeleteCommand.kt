package org.oreo.boatItemKiller.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable
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

                if (plugin.boatKillEnabled){
                    sender.sendMessage("${ChatColor.RED}This is already enabled!")
                    return true
                }

                plugin.boatKillEnabled = true
                sender.sendMessage("${ChatColor.AQUA}Boat deletion enabled!")

                deleteActiveBoats(sender)
                startItemEntityKiller(sender)
            }
            "disable" -> {

                if (!plugin.boatKillEnabled){
                    sender.sendMessage("${ChatColor.RED}This is already disabled!")
                    return true
                }

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
                if (boat.passengers.isEmpty()){
                    boat.remove()
                }
            }, plugin.boatDeleteDelay * 20L)

        }
    }

    private fun startItemEntityKiller(player: Player){
        val world = player.world
        val allPassives = world.getEntitiesByClasses(Sheep::class.java,Pig::class.java,Cow::class.java,
            Chicken::class.java,Pillager::class.java,Bee::class.java)



        object : BukkitRunnable() {
            override fun run() {
                // Cancel the task if boatKillEnabled is false
                if (!plugin.boatKillEnabled) {
                    cancel()
                    return
                }

                // Remove all passive mobs
                for (passiveMob in allPassives) {
                    passiveMob.remove()
                }

                // Remove all incorrect items from the world
                for (item in world.entities) {
                    // Check if the entity is an item
                    if (item is Item) {
                        if (isCorrectItem(item)) {
                            continue
                        }
                        item.remove()
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, plugin.entityItemKillDelay * 20L)
    }

    private fun isCorrectItem(item: Item) : Boolean{

        val allowedMaterials = setOf(
            Material.SNOWBALL,
            Material.WARPED_FUNGUS_ON_A_STICK,
            Material.CARROT_ON_A_STICK,

            Material.GOLDEN_HORSE_ARMOR,
            Material.IRON_HORSE_ARMOR,
            Material.LEATHER_HORSE_ARMOR,

            Material.PHANTOM_MEMBRANE,

            Material.GOLDEN_CARROT,
            Material.APPLE,
            Material.BREAD,
            Material.COOKED_BEEF,
            Material.COOKED_CHICKEN,
            Material.COOKED_COD,
            Material.COOKED_MUTTON,
            Material.COOKED_PORKCHOP,
            Material.COOKED_RABBIT,
            Material.COOKED_SALMON,
            Material.CARROT,
            Material.BAKED_POTATO,
            Material.POTATO,
            Material.PUMPKIN_PIE,
            Material.BEETROOT,
            Material.BEETROOT_SOUP,
            Material.COOKIE,
            Material.MELON_SLICE,
            Material.MUSHROOM_STEW,
            Material.RABBIT_STEW,
            Material.SUSPICIOUS_STEW,
            Material.DRIED_KELP,
            Material.TROPICAL_FISH,



            Material.WOODEN_SWORD,
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,

            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.IRON_HELMET,
            Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS,
            Material.IRON_BOOTS,
            Material.GOLDEN_HELMET,
            Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_LEGGINGS,
            Material.GOLDEN_BOOTS,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,

            Material.WOODEN_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE,

            Material.WOODEN_PICKAXE,
            Material.STONE_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE,

            Material.WOODEN_SHOVEL,
            Material.STONE_SHOVEL,
            Material.IRON_SHOVEL,
            Material.GOLDEN_SHOVEL,
            Material.DIAMOND_SHOVEL,
            Material.NETHERITE_SHOVEL,

            Material.OAK_FENCE,
            Material.SPRUCE_FENCE,
            Material.BIRCH_FENCE,
            Material.JUNGLE_FENCE,
            Material.ACACIA_FENCE,
            Material.DARK_OAK_FENCE,
            Material.MANGROVE_FENCE,
            Material.CHERRY_FENCE,
            Material.BAMBOO_FENCE,
            Material.CRIMSON_FENCE,
            Material.WARPED_FENCE,

        )

        return item.itemStack.type in allowedMaterials

    }
}
