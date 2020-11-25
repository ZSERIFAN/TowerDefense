package by.thmihnea.arena;

import org.bukkit.ChatColor;

public enum GameState {
    IN_EDITING("&eBeing Edited"),
    NEEDS_SETUP("&cAwaiting Arena Setup &7&o(Set Spawnpoints)"),
    WAITING("§aWaiting"),
    COUNTDOWN("&2Countdown"),
    IN_PROGRESS("§6In Game"),
    CLEANUP("§cCleanup");

    private String text;

    GameState(String text) {
        this.text = text;
    }

    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', this.text);
    }
}
