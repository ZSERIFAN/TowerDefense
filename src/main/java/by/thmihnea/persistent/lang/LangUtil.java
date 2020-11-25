package by.thmihnea.persistent.lang;

import by.thmihnea.TowerDefense;
import by.thmihnea.Util;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class LangUtil {

    public static void loadLang() {
        File lang = new File(TowerDefense.getInstance().getDataFolder(), "lang.yml");
        if (!lang.exists()) {
            try {
                TowerDefense.getInstance().getDataFolder().mkdir();
                lang.createNewFile();
                Reader defConfigStream = new InputStreamReader(TowerDefense.getInstance().getResource("lang.yml"), "UTF-8");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                    defConfig.save(lang);
                    Lang.setFile(defConfig);
                    return;
                }
            } catch(IOException e) {
                e.printStackTrace(); // So they notice
                Util.log(Util.LogType.ERROR, "Couldn't create language file lang.yml!");
                Util.log(Util.LogType.ERROR, "This is a fatal error. Disabling plugin...");
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for(Lang item:Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        TowerDefense.LANG = conf;
        TowerDefense.LANG_FILE = lang;
        try {
            conf.save(TowerDefense.getLangFile());
            Util.log(Util.LogType.INFO, "File lang.yml was successfully loaded from disk.");
        } catch(IOException e) {
            e.printStackTrace();
            Util.log(Util.LogType.ERROR, "Failed to enable lang.yml!");
            Util.log(Util.LogType.ERROR, "Please report this error to thmDev.");
        }
    }

}
