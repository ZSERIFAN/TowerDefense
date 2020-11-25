package by.thmihnea.listeners.arena;

import by.thmihnea.TowerDefense;
import by.thmihnea.Util;
import by.thmihnea.arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ArenaRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(e.getPlayer());
        if (arena == null) {
            Util.log(Util.LogType.WARNING, "A player has just died without being in an arena. How did this happen? PLAYER: " + e.getPlayer().getName());
            return;
        }
        Player p = e.getPlayer();
        if (arena.isDefender(p)) {
            e.setRespawnLocation(arena.getSpawnPoints().get("defender"));
        } else {
            e.setRespawnLocation(arena.getSpawnPoints().get("attacker"));
        }
        return;
    }
}
