package by.thmihnea.runnables;

import by.thmihnea.TowerDefense;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.player.TDPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

public class LavaRemovalTask implements Runnable {

    private Block block;
    private BukkitTask task;
    private long endTime;

    public LavaRemovalTask(Block block, long seconds) {
        this.block = block;
        this.endTime = System.currentTimeMillis() + (seconds * 1000);
        this.task = Bukkit.getScheduler().runTaskTimer(TowerDefense.getInstance(), this, 0L, 20L);
    }

    public void run() {
        if (isOver()) {
            this.clear();
            removeLava(this.block);
        }
    }

    public void removeLava(Block block) {
        block.setType(XMaterial.STONE_BRICKS.parseMaterial());
        for (int x = block.getX() - 1; x <= block.getX() + 1; x++) {
            for (int z = block.getZ() - 1; z <= block.getZ() + 1; z++) {
                Location location = new Location(block.getWorld(), x, block.getY(), z);
                Block bl = location.getBlock();
                if (bl.getType() == XMaterial.LAVA.parseMaterial())
                    removeLava(bl);
            }
        }
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
            task = null;
        }
    }

}
