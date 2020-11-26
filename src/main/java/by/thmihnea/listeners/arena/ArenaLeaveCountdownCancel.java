package by.thmihnea.listeners.arena;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.events.PlayerLeaveArenaEvent;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaLeaveCountdownCancel implements Listener {

    @EventHandler
    public void onArenaLeave(PlayerLeaveArenaEvent e) {
        Arena arena = e.getArena();
        Player p = e.getPlayer();
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        tdPlayer.setInGame(false);
        p.setHealth(20.0D);
        p.setFoodLevel(20);
        if (arena.getState().equals(GameState.COUNTDOWN)) {
            if (arena.getPlayers().size() < TowerDefense.cfg.getInt("arena.minimum_players_to_start_game")) {
                arena.stopCountdown();
                arena.broadCastMessage(Lang.COUNTDOWN_CANCELLED.toString());
            }
        }
    }
}
