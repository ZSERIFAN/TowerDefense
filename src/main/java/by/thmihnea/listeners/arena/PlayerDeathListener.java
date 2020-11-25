package by.thmihnea.listeners.arena;

import by.thmihnea.player.TDPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.getDrops().clear();
        Player p = e.getEntity();
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (!tdPlayer.isInGame()) return;
        e.setKeepInventory(true);
    }
}
