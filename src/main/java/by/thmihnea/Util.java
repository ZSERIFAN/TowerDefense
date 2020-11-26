package by.thmihnea;

import by.thmihnea.arena.Orb;
import by.thmihnea.commands.*;
import by.thmihnea.entity.*;
import by.thmihnea.inventory.shopitems.*;
import by.thmihnea.listeners.PlayerHandSwitchListener;
import by.thmihnea.listeners.PlayerInteractListener;
import by.thmihnea.listeners.arena.*;
import by.thmihnea.listeners.hub.*;
import by.thmihnea.persistent.config.ConfUtil;
import by.thmihnea.persistent.lang.LangUtil;
import by.thmihnea.player.TDPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class Util {

    public enum LogType {
        ERROR,
        INFO,
        WARNING
    }
    public static void log(LogType logType, String message) {
        switch (logType) {
            case ERROR:
                System.out.println("[TowerDefense] (ERROR | FATAL) - " + message);
                break;
            case INFO:
                System.out.println("[TowerDefense] (INFO) - " + message);
                break;
            case WARNING:
                System.out.println("[TowerDefense] (WARNING) - " + message);
                break;
        }
    }

    public static void setupFiles() {
        LangUtil.loadLang();
        ConfUtil.confSetup();
        if (!TowerDefense.arenaData.exists()) TowerDefense.arenaData.mkdir();
    }

    public static void registerCommands() {
        TDCommand tdCommand = new TDCommand(TowerDefense.getInstance());
        JoinCommand joinCommand = new JoinCommand(TowerDefense.getInstance());
        LeaveCommand leaveCommand = new LeaveCommand(TowerDefense.getInstance());
        BuildCommand buildCommand = new BuildCommand(TowerDefense.getInstance());
        TCCommand tcCommand = new TCCommand(TowerDefense.getInstance());
    }

    public static void registerEvents() {
        TowerDefense.getInstance().events = new Listener[] {
                new PlayerJoinListener(),
                new PlayerLeaveListener(),
                new ArenaGameStart(),
                new ArenaJoinCountdownStart(),
                new ArenaLeaveCountdownCancel(),
                new ArenaMatchTimerTick(),
                new PlayerHungerChangeListener(),
                new PlayerDamageListener(),
                new PlayerBlockBreakListener(),
                new PlayerInteractListener(),
                new EntitySpawnEvent(),
                new WeatherChangeListener(),
                new PlayerDeathListener(),
                new ItemDurabilityChangeListener(),
                new PlayerInventoryListener(),
                new ArenaRespawnListener(),
                new PlayerDamagePlayerListener(),
                new PlayerHandSwitchListener(),
                new ArenaWinByLeaveEvent(),
                new ArenaLeaveInPostgame(),
                new Fireball(),
                new WaterBucket(),
                new MilkBucket(),
                new LavaBucket(),
                new Wall(),
                new TNT(),
                new InvisibilityPotion(),
                new SewerKey1(),
                new SewerKey2(),
                new Orb(),
                new ArenaBreakOrbListener(),
                new ArenaChatListener(),
                new Cobweb(),
                new EntityDeathListener()
        };
        for (Listener listener : TowerDefense.getInstance().events)
            Bukkit.getPluginManager().registerEvents(listener, TowerDefense.getInstance());
    }

    public static void copyFileStructure(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyWorld(World originalWorld, String newWorldName) {
        copyFileStructure(originalWorld.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorldName));
        new WorldCreator(newWorldName).createWorld();
    }

    public static void setupObjects() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!TDPlayer.tdPlayers.containsKey(p.getUniqueId())) {
                TDPlayer tdPlayer = new TDPlayer(p);
                TDPlayer.tdPlayers.put(p.getUniqueId(), tdPlayer);
                Util.log(Util.LogType.INFO, "Initialized object " + tdPlayer + " for player " + p.getName() + ". (UUID: " + p.getUniqueId() + ")");
            }
        }
    }

    public static void registerEntities() {
        NMSUtil nmsu = new NMSUtil();

        nmsu.registerEntity("Royal Zombie", 54, EntityZombie.class, Zombie.class);
        nmsu.registerEntity("Undead Archer", 51, EntitySkeleton.class, Skeleton.class);
        nmsu.registerEntity("Royal Wolf", 95, EntityWolf.class, Wolf.class);
        nmsu.registerEntity("Might Golem", 99, EntityIronGolem.class, Golem.class);
    }

    public static boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object)
    {
        Field field;
        Object o = null;

        try
        {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return o;
    }

}
