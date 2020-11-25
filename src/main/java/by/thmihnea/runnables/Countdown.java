package by.thmihnea.runnables;

import by.thmihnea.TowerDefense;
import by.thmihnea.Util;
import by.thmihnea.arena.Arena;
import by.thmihnea.persistent.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Countdown extends BukkitRunnable {

    private Arena arena;
    int seconds = 20;

    public void broadcastArenaMessage(String message) {
        for (UUID uuid : arena.getPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            p.sendMessage(message);
        }
    }

    public Countdown(Arena arena) {
        this.arena = arena;
        broadcastArenaMessage(Lang.MATCH_STARTING_IN_20.toString().replace("%time%", String.valueOf(seconds)));
        this.runTaskTimer(TowerDefense.getInstance(), 0L, 20L);
    }

    public void run() {
        seconds--;
        switch (seconds) {
            case 10:
                broadcastArenaMessage(Lang.MATCH_STARTING_IN_10.toString().replace("%time%", String.valueOf(seconds)));
                break;
            case 5:
                broadcastArenaMessage(Lang.MATCH_STARTING_IN_5.toString().replace("%time%", String.valueOf(seconds)));
                break;
            case 3:
                broadcastArenaMessage(Lang.MATCH_STARTING_IN_3.toString().replace("%time%", String.valueOf(seconds)));
                break;
            case 2:
                broadcastArenaMessage(Lang.MATCH_STARTING_IN_2.toString().replace("%time%", String.valueOf(seconds)));
                break;
            case 1:
                broadcastArenaMessage(Lang.MATCH_STARTING_IN_1.toString().replace("%time%", String.valueOf(seconds)));
                break;
        }
        if (seconds <= 0) {
            Util.log(Util.LogType.INFO, "Started a game in arena " + arena.getName() + ". (" + arena.getPlayers().size() + "/5 PLAYERS)");
            arena.startGame();
            this.cancel();
        }
    }
}
