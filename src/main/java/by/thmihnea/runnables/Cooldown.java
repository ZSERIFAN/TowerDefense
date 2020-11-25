package by.thmihnea.runnables;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.player.TDPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Cooldown implements Runnable {

    private Arena arena;
    private Player player;
    private CooldownType type;
    private long endTime;
    private BukkitTask task;
    private long seconds;

    public Cooldown(Arena arena, Player player, CooldownType cooldownType, long seconds) {
        this.seconds = seconds;
        this.arena = arena;
        this.player = player;
        this.type = cooldownType;
        this.endTime = System.currentTimeMillis() + (seconds * 1000);
        task = Bukkit.getScheduler().runTaskTimer(TowerDefense.getInstance(), this, 0L, 20L);
    }

    public void run() {
        if (isOver()) {
            clear();
        }
    }

    public void resetTime() {
        this.endTime = System.currentTimeMillis() + (this.seconds * 1000);
    }

    public long timeLeft() {
        return (endTime - System.currentTimeMillis());
    }

    public long timeLeftInSeconds() {
        return timeLeft() / 1000;
    }

    public boolean isOver() {
        return System.currentTimeMillis() >= endTime;
    }

    public void extendTime(long seconds) {
        this.endTime = endTime + (seconds * 1000);
    }

    public void clear() {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
            TDPlayer tdp = TDPlayer.tdPlayers.get(this.player.getUniqueId());
            tdp.removeCooldown(this);
            task = null;
        }
    }

    public CooldownType getCooldownType() {
        return this.type;
    }
}
