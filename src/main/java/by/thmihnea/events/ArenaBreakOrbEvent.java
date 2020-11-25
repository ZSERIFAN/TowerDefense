package by.thmihnea.events;

import by.thmihnea.arena.Arena;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaBreakOrbEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private Player player;
    private Integer orbNumber;
    private Block block;

    public ArenaBreakOrbEvent(Arena arena, Player player, Integer orbNumber, Block block) {
        this.arena = arena;
        this.player = player;
        this.orbNumber = orbNumber;
        this.block = block;
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
        return this.player;
    }

    public Integer getOrbNumber() {
        return this.orbNumber;
    }

    public Block getBlock() {
        return this.block;
    }

}
