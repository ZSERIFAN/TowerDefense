package by.thmihnea.runnables;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class PostgameTimer implements Runnable {

    private Arena arena;
    private long endTime;
    private BukkitTask task;

    public PostgameTimer(Arena arena, long seconds) {
        this.arena = arena;
        this.endTime = System.currentTimeMillis() + (seconds * 1000);
        task = Bukkit.getScheduler().runTaskTimer(TowerDefense.getInstance(), this, 0L, 20L);
    }

    public long timeLeft() {
        return (endTime - System.currentTimeMillis());
    }

    public long timeLeftInSeconds() {
        return timeLeft() / 1000;
    }

    void sendActionBar() {
        int minutes = 0;
        long seconds = this.timeLeftInSeconds();
        if (seconds <= 0) return;
        while (seconds - 60 >= 0) {
            minutes++;
            seconds -= 60;
        }
        String aux = "";
        String minAux = "";
        if (seconds < 10) aux = "0";
        if (minutes < 10) minAux = "0";
        this.arena.broadCastActionBar("§6• §fPost Game: §e" + minAux + minutes + "§8:§e" + aux + seconds + " §6•", false);
    }

    public void run() {
        if (isOver()) {
            clear();
            arena.resetArena();
        } else
            sendActionBar();
    }

    public boolean isOver() {
        return System.currentTimeMillis() >= endTime;
    }

    public void clear() {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
            task = null;
        }
    }
}
