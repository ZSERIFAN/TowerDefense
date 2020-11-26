package by.thmihnea.runnables;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

public class OrbReplace implements Runnable {

    private Arena arena;
    private long endTime;
    private BukkitTask task;
    private Block block;
    private Integer orbBreak;

    public OrbReplace(Arena arena, Block block, int orbBreak, long seconds) {
        this.arena = arena;
        this.block = block;
        this.orbBreak = orbBreak;
        this.endTime = System.currentTimeMillis() + (seconds * 1000);
        task = Bukkit.getScheduler().runTaskTimer(TowerDefense.getInstance(), this, 0L, 20L);
    }

    public void run() {
        if (isOver()) {
            clear();
            block.setType(XMaterial.GLASS.parseMaterial());
            this.arena.broadCastMessage("§c§l(!) §6The orb has respawned! Break it " + (3 - orbBreak) + " more time(s) to defeat the Defenders!");
        }
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
