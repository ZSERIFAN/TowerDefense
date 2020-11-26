package by.thmihnea.runnables;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.util.item.ArmorUtil;
import by.thmihnea.util.item.ShopItemUtil;
import com.connorlinfoot.bountifulapi.BountifulAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Respawn implements Runnable {

    private Arena arena;
    private Player player;
    private long endTime;
    private BukkitTask task;

    public Respawn(Player player, Arena arena, long seconds) {
        this.arena = arena;
        this.player = player;
        this.endTime = System.currentTimeMillis() + (seconds * 1000);
        task = Bukkit.getScheduler().runTaskTimer(TowerDefense.getInstance(), this, 0L, 20L);
    }

    public void run() {
        if (isOver()) {
            clear();
            player.setGameMode(GameMode.SURVIVAL);
            if (arena.isDefender(player)) {
                player.teleport(arena.getSpawnPoints().get("defender"));
            } else {
                player.teleport(arena.getSpawnPoints().get("attacker"));
            }
            for (PotionEffect effect : new ArrayList<PotionEffect>(player.getActivePotionEffects()))
                player.removePotionEffect(effect.getType());
            ShopItemUtil.giveShop(player);
        }
        sendTitle();
    }

    void sendTitle() {
        long seconds = this.timeLeftInSeconds();
        if (seconds < 0) return;
        String aux = "";
        if (seconds < 10) aux = "0";
        BountifulAPI.sendTitle(this.player, 5, 10, 5, "§fRespawning in", "§e00:" + aux + seconds);
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
