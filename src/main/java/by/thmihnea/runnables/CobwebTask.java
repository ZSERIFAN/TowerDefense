package by.thmihnea.runnables;

import by.thmihnea.TowerDefense;
import by.thmihnea.multiversion.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitTask;

public class CobwebTask implements Runnable {

    private Item item;
    private BukkitTask task;

    public CobwebTask(Item item) {
        this.item = item;
        task = Bukkit.getScheduler().runTaskTimer(TowerDefense.getInstance(), this, 0L, 1L);
    }

    public void run() {
        if (this.item.isOnGround()) {
            this.clear();
            Location location = this.item.getLocation();
            this.item.remove();
            location.getBlock().setType(XMaterial.COBWEB.parseMaterial());
        }
    }

    public void clear() {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
            task = null;
        }
    }
}
