package by.thmihnea.listeners.arena;

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
        if (arena.getPlayers().size() >= 2)
            arena.setState(GameState.COUNTDOWN);
    }
}
