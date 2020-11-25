package by.thmihnea.listeners.arena;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class ItemDurabilityChangeListener implements Listener {

    @EventHandler
    public void onDurabilityChange(PlayerItemDamageEvent e) {
        if (e.isCancelled()) return;
        e.setCancelled(true);
    }
}
