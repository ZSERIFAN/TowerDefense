package by.thmihnea.listeners.hub;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawnEvent implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) e.setCancelled(true);
    }
}
