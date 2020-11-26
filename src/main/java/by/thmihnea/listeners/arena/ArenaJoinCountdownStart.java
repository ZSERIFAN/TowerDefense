package by.thmihnea.listeners.arena;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.events.PlayerJoinArenaEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaJoinCountdownStart implements Listener {

    @EventHandler
    public void onArenaJoin(PlayerJoinArenaEvent e) {
        Arena arena = e.getArena();
        if (arena.getState().equals(GameState.COUNTDOWN)) return;
        if (arena.getPlayers().size() >= TowerDefense.cfg.getInt("arena.minimum_players_to_start_game"))
            arena.setState(GameState.COUNTDOWN);
    }
}
