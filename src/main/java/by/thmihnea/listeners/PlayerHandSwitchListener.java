package by.thmihnea.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerHandSwitchListener implements Listener {

    @EventHandler
    public void onHand(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);
    }
}
