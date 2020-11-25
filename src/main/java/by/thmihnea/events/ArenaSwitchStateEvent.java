package by.thmihnea.events;

import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaSwitchStateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private GameState previousState;

    public ArenaSwitchStateEvent(Arena arena, GameState previousState) {
        this.arena = arena;
        this.previousState = previousState;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Arena getArena() {
        return arena;
    }

    public GameState getCurrentState() {
        return arena.getState();
    }

    public GameState getPreviousState() {
        return previousState;
    }

}
