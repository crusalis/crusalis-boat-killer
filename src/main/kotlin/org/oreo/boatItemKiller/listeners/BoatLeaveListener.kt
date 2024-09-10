package org.oreo.boatItemKiller.listeners

import org.bukkit.Bukkit
import org.bukkit.entity.Boat
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.oreo.boatItemKiller.BoatItemKiller
import org.spigotmc.event.entity.EntityDismountEvent

class BoatLeaveListener(private val plugin: BoatItemKiller) : Listener {

    @EventHandler
    fun playerExitBoat(e:EntityDismountEvent){


        if (!plugin.boatKillEnabled){
            return
        }

        val boat = e.dismounted

        if (boat !is Boat){
            return
        }

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            if (boat.passengers.isEmpty()){
                runDeletionTimer(boat)
            }

        }, plugin.boatDelayAfterPlacing * 20L)
    }

    private fun runDeletionTimer(boat: Entity){
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            boat.remove()
        }, plugin.boatDeleteDelay * 20L)
    }
}