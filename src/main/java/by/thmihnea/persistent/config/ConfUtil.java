package by.thmihnea.persistent.config;

import by.thmihnea.TowerDefense;
import by.thmihnea.Util;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

public class ConfUtil {
    private static File dir = new File("plugins", "thmSkills");

    public static void confSetup() {
        if (!dir.exists()) {
            Util.log(Util.LogType.INFO, "Directory plugins/thmSkills was not found. Creating new file...");
            dir.mkdir();
        }
        if (!TowerDefense.config.exists()) {
            Util.log(Util.LogType.INFO, "Didn't find file config.yml on disk. Creating new file...");
            TowerDefense.getInstance().saveDefaultConfig();
        }
        try {
            TowerDefense.cfg.load(TowerDefense.config);
            Util.log(Util.LogType.INFO, "File config.yml was successfully loaded from disk.");
        } catch (IOException e) {
            //
        } catch (InvalidConfigurationException e) {
            Util.log(Util.LogType.ERROR, "Couldn't load default config for plugin thmSkills.jar (INVALID_CONFIGURATION)");
        }
    }
}
