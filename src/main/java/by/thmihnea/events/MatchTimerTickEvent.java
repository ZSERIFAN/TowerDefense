package by.thmihnea.events;

import by.thmihnea.runnables.GameTimer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchTimerTickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private GameTimer gameTimer;

    public MatchTimerTickEvent(GameTimer gameTimer) {
        this.gameTimer = gameTimer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GameTimer getMatchTimer() {
        return gameTimer;
    }
}
