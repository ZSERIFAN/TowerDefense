package by.thmihnea.listeners.hub;

import by.thmihnea.Util;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.util.item.ArmorUtil;
import by.thmihnea.util.item.SelectorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.getInventory().clear();
        ArmorUtil.clearArmor(p);
        p.setHealth(20.0D);
        p.setFoodLevel(20);
        TDPlayer tdp = new TDPlayer(p);
        TDPlayer.tdPlayers.put(p.getUniqueId(), tdp);
        Util.log(Util.LogType.INFO, "Initialized object " + tdp + " for player " + p.getName() + ". (UUID: " + p.getUniqueId() + ")");
        File hubFile = new File("plugins/TowerDefense/gameHub.yml");
        if (hubFile.exists()) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(hubFile);
            p.teleport(new Location(Bukkit.getWorld(cfg.getString("hub.spawnLocation.world")), cfg.getDouble("hub.spawnLocation.x"), cfg.getDouble("hub.spawnLocation.y"), cfg.getDouble("hub.spawnLocation.z"), (float) cfg.getDouble("hub.spawnLocation.pitch"), (float) cfg.getDouble("hub.spawnLocation.yaw")));
        }
        SelectorUtil.giveSelector(p);
    }
}
