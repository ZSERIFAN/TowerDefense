package by.thmihnea.listeners.hub;

import by.thmihnea.Util;
import by.thmihnea.player.TDPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        p.getInventory().clear();
        p.performCommand("leave");
        if (TDPlayer.tdPlayers.containsKey(e.getPlayer().getUniqueId())) {
            Util.log(Util.LogType.INFO, "Deleted player " + e.getPlayer().getName() + " from cached data. (UUID: " + e.getPlayer().getUniqueId() + ")");
            TDPlayer.tdPlayers.remove(e.getPlayer().getUniqueId());
        } else {
            Util.log(Util.LogType.INFO, "Couldn't find TD Object for player " + e.getPlayer().getName());
        }
    }
}
