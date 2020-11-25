package by.thmihnea.runnables;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.events.MatchTimerTickEvent;
import by.thmihnea.persistent.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class GameTimer implements Runnable {

    private Arena arena;
    private long endTime;
    private BukkitTask task;

    public GameTimer(Arena arena, long seconds) {
        this.arena = arena;
        this.endTime = System.currentTimeMillis() + (seconds * 1000);
        task = Bukkit.getScheduler().runTaskTimer(TowerDefense.getInstance(), this, 0L, 20L);
    }

    public void run() {
        if (isOver()) {
            clear();
            if (arena.getState() == GameState.IN_PROGRESS) {
                arena.broadCastMessage(Lang.MATCH_ENDED.toString());
                arena.setState(GameState.CLEANUP);
                PostgameTimer postgameTimer = new PostgameTimer(this.arena, 30);
                arena.setPostgameTimer(postgameTimer);
            }
            arena.broadCastMessage(Lang.ARENA_WIN_DEFENDERS.toString());
        }
        MatchTimerTickEvent e = new MatchTimerTickEvent(this);
        Bukkit.getPluginManager().callEvent(e);
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

    public long timeLeft() {
        return (endTime - System.currentTimeMillis());
    }

    public long timeLeftInSeconds() {
        return timeLeft() / 1000;
    }

    public void extendTime(long seconds) {
        this.endTime = endTime + (seconds * 1000);
    }

    public Arena getArena() {
        return arena;
    }

    public long getEndTime() {
        return endTime;
    }
}
