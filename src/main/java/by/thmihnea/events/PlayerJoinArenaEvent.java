package by.thmihnea.events;

import by.thmihnea.arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinArenaEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private Player player;

    public PlayerJoinArenaEvent (Arena arena, Player player) {
        this.player = player;
        this.arena = arena;
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

    public Player getPlayer() {
        return player;
    }
}
