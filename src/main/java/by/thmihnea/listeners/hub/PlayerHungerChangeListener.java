package by.thmihnea.listeners.hub;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungerChangeListener implements Listener {

    @EventHandler
    public void HungerChange(FoodLevelChangeEvent e) {
        if (e.isCancelled()) return;
        LivingEntity entity = e.getEntity();
        if (!(entity instanceof Player)) return;
        e.setCancelled(true);
    }

}
