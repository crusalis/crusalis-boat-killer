package org.oreo.boatItemKiller

import org.bukkit.plugin.java.JavaPlugin
import org.oreo.boatItemKiller.commands.BoatDeleteCommand
import org.oreo.boatItemKiller.listeners.BoatLeaveListener
import org.oreo.boatItemKiller.listeners.BoatPlaceListener

class BoatItemKiller : JavaPlugin() {

    var boatKillEnabled = false
    val boatDeleteDelay = config.getInt("boat-delete-delay")
    val boatDelayAfterPlacing = config.getInt("boat-delay-after-placing")
    val entityItemKillDelay = config.getInt("entity-item-kill-delay")

    override fun onEnable() {
        server.pluginManager.registerEvents(BoatPlaceListener(this),this)
        server.pluginManager.registerEvents(BoatLeaveListener(this),this)

        getCommand("boatKill")!!.setExecutor(BoatDeleteCommand(this))

        saveDefaultConfig()
    }

}
