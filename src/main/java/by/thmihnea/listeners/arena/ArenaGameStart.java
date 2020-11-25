package by.thmihnea.listeners.arena;

import by.thmihnea.arena.GameState;
import by.thmihnea.events.ArenaSwitchStateEvent;
import by.thmihnea.runnables.Countdown;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaGameStart implements Listener {

    @EventHandler
    public void onStateChange(ArenaSwitchStateEvent e) {
        if (e.getCurrentState() != GameState.COUNTDOWN) return;
        Countdown cnt = new Countdown(e.getArena());
        e.getArena().setCountdown(cnt);
    }
}
