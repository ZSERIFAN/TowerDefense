package by.thmihnea.runnables;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Particles implements Runnable {

    private Arena arena;
    private BukkitTask task;

    public Particles(Arena arena) {
        this.arena = arena;
        task = Bukkit.getScheduler().runTaskTimer(TowerDefense.getInstance(), this, 0L, 20L);
    }

    public void clear() {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
            task = null;
        }
    }

    @Override
    public void run() {
        if (arena.getTraps().isEmpty()) return;
        for (UUID uuid : arena.getPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (arena.isAttacker(p)) continue;
            for (Block block : arena.getTraps()) {
                Location location = block.getLocation().add(0, 1, 0);
                p.playEffect(location, Effect.SMOKE, 5);
            }
        }
    }
}
