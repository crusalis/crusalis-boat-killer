package org.oreo.boatItemKiller.listeners

import org.bukkit.Bukkit
import org.bukkit.block.BlockFace
import org.bukkit.entity.Boat
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.oreo.boatItemKiller.BoatItemKiller
import org.spigotmc.event.entity.EntityDismountEvent

class BoatPlaceListener(private val plugin: BoatItemKiller) : Listener {

    @EventHandler
    fun playerExitBoat(e:EntityPlaceEvent){

        if (!plugin.boatKillEnabled || e.entity !is Boat){
            return
        }

        val boat = e.entity

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {

            if (boat.passengers.isEmpty()){
                runDeletionTimer(boat)
            }

        }, plugin.boatDelayAfterPlacing * 20L)
    }

    private fun runDeletionTimer(boat:Entity){
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            boat.remove()
        }, plugin.boatDeleteDelay * 20L)
    }
}