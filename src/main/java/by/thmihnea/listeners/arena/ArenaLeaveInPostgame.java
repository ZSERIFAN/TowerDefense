package by.thmihnea.listeners.arena;

import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.events.PlayerLeaveArenaEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaLeaveInPostgame implements Listener {

    @EventHandler
    public void onArenaLeave(PlayerLeaveArenaEvent e) {
        Arena arena = e.getArena();
        Player p = e.getPlayer();

        if (!arena.getState().equals(GameState.CLEANUP)) return;

        // TODO process rewards before arena cleanup
    }
}
