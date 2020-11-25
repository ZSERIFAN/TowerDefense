package by.thmihnea.arena;

import by.thmihnea.TowerDefense;
import by.thmihnea.Util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class ArenaHandler {

    private TowerDefense plugin;
    private Map<Integer, Arena> arenas;

    public ArenaHandler(TowerDefense plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<Integer, Arena>();
        loadArenas();
    }

    private void loadArenas() {
        int cnt = 0;
        File[] arenaFiles = TowerDefense.arenaData.listFiles();
        if (arenaFiles.length != 0) {
            for (File file : arenaFiles) {
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                Arena arena = new Arena(cfg.getInt("arena.id"), cfg.getString("arena.name"), Bukkit.getWorld(cfg.getString("arena.world")), cfg.getInt("arena.timeLimit"), file);
                if (arenaAlreadyExists(arena.getName())) {
                    Util.log(Util.LogType.INFO, "Found duplicate arena " + arena.getName() + " in arenaData. Unloading this arena to not cause any bugs.");
                    continue;
                }
                addArena(arena);
                cnt++;
            }
            Util.log(Util.LogType.INFO, "Successfully loaded a total of " + cnt + " arenas!");
        } else {
            Util.log(Util.LogType.INFO, "No arenas found in the arenaData folder, therefore no arenas were loaded.");
        }
    }

    public void addArena(Arena arena) {
        if (!arenas.containsKey(arena))
            arenas.put(arena.getId(), arena);
    }

    public void removeArena(Arena arena) {
        if (arenas.containsKey(arena.getId()))
            arenas.remove(arena.getId(), arena);
    }

    public Set<Arena> getArenas() {
        return new HashSet<>(this.arenas.values());
    }

    public Arena getArenaByPlayer(Player player) {
        for (Arena arena : this.arenas.values())
            if (player.getWorld().equals(arena.getWorld()))
                return arena;
        return null;
    }

    public Arena getArenaById(Integer id) {
        if (this.arenas.containsKey(id))
            return this.arenas.get(id);
        return null;
    }

    public Arena getArenaByWorld(String world) {
        for (Arena arena : this.arenas.values())
            if (arena.getWorld().equals(Bukkit.getWorld(world)))
                return arena;
        return null;
    }

    public Arena getArenaByName(String name) {
        for (Arena arena : this.arenas.values())
            if (arena.getName().equalsIgnoreCase(name))
                return arena;
        return null;
    }

    public boolean arenaAlreadyExists(String name) {
        for (Arena arena : this.arenas.values())
            if (arena.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }

    public boolean arenaAlreadyExistsWorld(World world) {
        for (Arena arena : this.arenas.values())
            if (arena.getWorld().equals(world))
                return true;
        return false;
    }
}
