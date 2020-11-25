package by.thmihnea.commands;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.util.item.ArmorUtil;
import by.thmihnea.util.item.SelectorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class LeaveCommand implements CommandExecutor {

    private TowerDefense plugin;

    public LeaveCommand(TowerDefense plugin) {
        this.plugin = plugin;

        plugin.getCommand("leave").setExecutor(this);
    }

    public boolean isInGame(Player p) {
        for (Arena arena : plugin.arenaHandler.getArenas())
            if (arena.inArena(p)) return true;
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (!isInGame(p)) {
            p.sendMessage(Lang.MUST_BE_IN_GAME_TO_LEAVE.toString());
            return true;
        }
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        if (arena.getState().equals(GameState.IN_PROGRESS)) {
            arena.removeFromTeam(p);
            tdPlayer.setInGame(false);
        }
        arena.removePlayer(p);
        if (arena.getState().equals(GameState.COUNTDOWN))
            if (arena.getPlayers().size() < 4)
                arena.setState(GameState.WAITING);
        File file = new File("plugins/TowerDefense/gameHub.yml");
        if (file.exists()) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            p.teleport(new Location(Bukkit.getWorld(cfg.getString("hub.spawnLocation.world")), cfg.getDouble("hub.spawnLocation.x"), cfg.getDouble("hub.spawnLocation.y"), cfg.getDouble("hub.spawnLocation.z"), (float) cfg.getDouble("hub.spawnLocation.pitch"), (float) cfg.getDouble("hub.spawnLocation.yaw")));
        }
        else p.teleport(Bukkit.getWorld("world").getSpawnLocation());
        p.sendMessage(Lang.LEFT_ARENA.toString().replace("%arena%", arena.getName()));
        p.getInventory().clear();
        ArmorUtil.clearArmor(p);
        SelectorUtil.giveSelector(p);
        if (!arena.getPlayers().isEmpty()) {
            for (UUID uuid : arena.getPlayers()) {
                Player i = Bukkit.getPlayer(uuid);
                i.sendMessage(Lang.PLAYER_LEFT_YOUR_ARENA.toString().replace("%player%", p.getName()).replace("%inArena%", String.valueOf(arena.getPlayers().size())));
            }
        }
        return false;
    }
}
