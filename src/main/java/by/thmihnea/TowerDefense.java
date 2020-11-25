package by.thmihnea;

import by.thmihnea.arena.ArenaHandler;;
import org.apache.commons.lang.time.StopWatch;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TowerDefense extends JavaPlugin {

    private static TowerDefense instance;
    public static TowerDefense getInstance() {
        return instance;
    }

    public static YamlConfiguration LANG;
    public static File LANG_FILE;
    public static File config = new File("plugins/TowerDefense/config.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(config);
    public static File arenaData = new File("plugins/TowerDefense/arenaData");

    public static ArenaHandler arenaHandler;

    public Listener[] events;

    @Override
    public void onEnable() {
        StopWatch sw = new StopWatch();
        sw.start();
        Util.log(Util.LogType.INFO, "Beggining plugin setup.");
        instance = this;
        Util.setupFiles();
        Util.setupObjects();
        Util.registerCommands();
        Util.registerEvents();
        Util.registerEntities();

        this.arenaHandler = new ArenaHandler(this);
        sw.stop();
        Util.log(Util.LogType.INFO, "Plugin initialization done! Process took: " + sw.getTime() + "ms");
    }

    @Override
    public void onDisable() {
        Util.log(Util.LogType.INFO, "Plugin successfully disabled. Goodbye!");
    }

    public ArenaHandler getArenaHandler() {
        return this.arenaHandler;
    }

    public static File getLangFile() {
        return LANG_FILE;
    }

}
