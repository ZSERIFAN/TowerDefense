package by.thmihnea.commands;

import by.thmihnea.TowerDefense;
import by.thmihnea.Util;
import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.util.item.SelectorUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TDCommand implements CommandExecutor {

    private TowerDefense plugin;
    public Map<UUID, Arena> editors = new HashMap<UUID, Arena>();

    public TDCommand(TowerDefense plugin) {
        this.plugin = plugin;

        plugin.getCommand("td").setExecutor(this);
    }

    public void showInfo(Player p, Arena arena) {
        if (arena == null) {
            p.sendMessage(Lang.ARENA_NOT_FOUND.toString());
            return;
        }
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_LAST.toString());
        p.sendMessage("§7§oDisplaying arena info");
        p.sendMessage("§eName §6- §f" + arena.getName());
        p.sendMessage("§eID §6- §f" + arena.getId());
        p.sendMessage("§eGame State §6- §f" + arena.getState().toString());
        p.sendMessage("§eWorld §6- §f" + arena.getWorld().getName());
        p.sendMessage("§eTime Limit §6 - §f" + arena.getTimeLimit());
        p.sendMessage("§fThere's currently §e" + arena.getPlayers().size() + "§7/§e5 §fplayers on this arena.");
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_LAST.toString());
    }

    public void switchEdit(Player p, Arena a) {
        File hubFile = new File("plugins/TowerDefense/gameHub.yml");
        TDPlayer tdp = TDPlayer.tdPlayers.get(p.getUniqueId());
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(hubFile);
        if (editors.containsKey(p.getUniqueId()) && editors.get(p.getUniqueId()) != a) {
            p.sendMessage("§c§l(!) §fYou can't edit multiple arenas at once! Please type §e/td arena edit §n" + editors.get(p.getUniqueId()).getName() + "§f to stop editing the current arena.");
            return;
        }
        if (!editors.containsKey(p.getUniqueId())) {
            tdp.setEditing(true);
            tdp.setCanBypassBuild(true);
            editors.put(p.getUniqueId(), a);
            a.setState(GameState.IN_EDITING);
            p.sendMessage(Lang.ENABLED_EDITOR.toString().replace("%arena%", a.getName()));
            Location location = a.getWorld().getSpawnLocation();
            if (YamlConfiguration.loadConfiguration(a.getArenaFile()).contains("arena.lobbyLocation"))
                p.teleport(a.getSpawnPoints().get("lobby"));
            else p.teleport(location);
            p.getInventory().clear();
            p.setGameMode(GameMode.CREATIVE);
        }
        else if (editors.containsKey(p.getUniqueId())) {
            tdp.setEditing(false);
            tdp.setCanBypassBuild(false);
            String name = a.getWorld().getName();
            editors.remove(p.getUniqueId());
            if (a.getSpawnPoints().size() >= 3)
                a.setState(GameState.WAITING);
            else
                a.setState(GameState.NEEDS_SETUP);
            p.sendMessage(Lang.DISABLED_EDITOR.toString());
            p.teleport(new Location(Bukkit.getWorld(cfg.getString("hub.spawnLocation.world")), cfg.getDouble("hub.spawnLocation.x"), cfg.getDouble("hub.spawnLocation.y"), cfg.getDouble("hub.spawnLocation.z"), (float) cfg.getDouble("hub.spawnLocation.pitch"), (float) cfg.getDouble("hub.spawnLocation.yaw")));
            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();
            SelectorUtil.giveSelector(p);
            Bukkit.getWorld(a.getWorld().getName()).save();
            Util.log(Util.LogType.INFO, "Copying world data for world " + a.getWorld().getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload copy_" + a.getWorld().getName());
            for (Entity entity : a.getWorld().getEntities()) entity.remove();
            Util.copyFileStructure(a.getWorld().getWorldFolder(), new File(Bukkit.getWorldContainer(), "copy_" + a.getWorld().getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv load copy_" + name);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import copy_" + name + " NORMAL");
        }
    }

    public void listArenas(Player p) {
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_LAST.toString());
        for (Arena arena : plugin.arenaHandler.getArenas()) {
            TextComponent message = new TextComponent();
            if (arena.getState().equals(GameState.IN_EDITING) || arena.getState().equals(GameState.CLEANUP) || arena.getState().equals(GameState.IN_PROGRESS))
                message.setText("§6➢ §f" + arena.getName() + " §6- " + arena.getState());
            else
                message.setText("§6➢ §f" + arena.getName() + " §6- " + arena.getState() + " §7§o(Click to edit this arena)");
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oBy clicking this you will be prompted to the edit menu.").create()));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/td arena edit " + arena.getName()));
            p.spigot().sendMessage(message);
        }
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_LAST.toString());
    }

    public boolean isInGame(Player p) {
        for (Arena arena : plugin.arenaHandler.getArenas())
            if (arena.inArena(p)) return true;
        return false;
    }

    public void sendHelp(Player p) {
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_1.toString());
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_2.toString());
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_3.toString());
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_4.toString());
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_5.toString());
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_6.toString());
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_7.toString());
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_8.toString());
        p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_LAST.toString());
    }

    public void createArena(String name, Integer id, String world, Integer timeLimit) throws IOException {
        World worldObj = Bukkit.getWorld(world);
        File arenaFile = new File("plugins/TowerDefense/arenaData/" + name + "-" + id);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(arenaFile);
        cfg.set("arena.name", name);
        cfg.set("arena.id", id);
        cfg.set("arena.world", world);
        cfg.set("arena.timeLimit", timeLimit);
        cfg.save(arenaFile);
        Arena arena = new Arena(id, name, worldObj, timeLimit, arenaFile);
        arena.setState(GameState.NEEDS_SETUP);
        plugin.arenaHandler.addArena(arena);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!(p.hasPermission("td.admin"))) {
            p.sendMessage(Lang.USER_IS_NOT_ADMIN.toString().replace("%version%", plugin.getDescription().getVersion()));
            return true;
        }

        switch (args.length) {
            case 0:
            case 1:
                sendHelp(p);
                return true;
            case 2:
                switch (args[0]) {
                    case "arena":
                        switch (args[1]) {
                            case "create":
                                p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_ARENA.toString().replace("%amount%", String.valueOf(args.length)));
                                p.sendMessage(Lang.COMMAND_TD_ARENA_USAGE.toString());
                                return true;
                            case "info":
                                p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_INFO.toString());
                                return true;
                            case "list":
                                listArenas(p);
                                return true;
                            case "edit":
                                p.sendMessage(Lang.INCORRECT_USAGE_EDIT.toString());
                                return true;
                            case "remove":
                                p.sendMessage(Lang.COMMAND_TD_ARENA_REMOVE_USAGE.toString());
                                return true;
                            default:
                                sendHelp(p);
                                return true;
                        }
                    case "setspawnpoint":
                        if (!this.editors.containsKey(p.getUniqueId()) && !args[1].equalsIgnoreCase("hub")) {
                            p.sendMessage(Lang.NOT_EDITING_ARENA.toString());
                            return true;
                        }
                        switch (args[1]) {
                            case "attacker":
                                Arena arena = this.editors.get(p.getUniqueId());
                                File arenaFile = arena.getArenaFile();
                                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(arenaFile);
                                cfg.set("arena.attackerLocation.world", p.getLocation().getWorld().getName());
                                cfg.set("arena.attackerLocation.x", p.getLocation().getX());
                                cfg.set("arena.attackerLocation.y", p.getLocation().getY());
                                cfg.set("arena.attackerLocation.z", p.getLocation().getZ());
                                cfg.set("arena.attackerLocation.pitch", p.getLocation().getPitch());
                                cfg.set("arena.attackerLocation.yaw", p.getLocation().getYaw());
                                try {
                                    cfg.save(arenaFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena.addSpawnPoint(p.getLocation(), "attacker");
                                p.sendMessage(Lang.ATTACKER_SPAWNPOINT_SET.toString());
                                return true;
                            case "defender":
                                Arena arena1 = this.editors.get(p.getUniqueId());
                                File arenaFile1 = arena1.getArenaFile();
                                YamlConfiguration cfg1 = YamlConfiguration.loadConfiguration(arenaFile1);
                                cfg1.set("arena.defenderLocation.world", p.getLocation().getWorld().getName());
                                cfg1.set("arena.defenderLocation.x", p.getLocation().getX());
                                cfg1.set("arena.defenderLocation.y", p.getLocation().getY());
                                cfg1.set("arena.defenderLocation.z", p.getLocation().getZ());
                                cfg1.set("arena.defenderLocation.pitch", p.getLocation().getPitch());
                                cfg1.set("arena.defenderLocation.yaw", p.getLocation().getYaw());
                                try {
                                    cfg1.save(arenaFile1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena1.addSpawnPoint(p.getLocation(), "defender");
                                p.sendMessage(Lang.DEFENDER_SPAWNPOINT_SET.toString());
                                return true;
                            case "lobby":
                                Arena arena2 = this.editors.get(p.getUniqueId());
                                File arenaFile2 = arena2.getArenaFile();
                                YamlConfiguration cfg2 = YamlConfiguration.loadConfiguration(arenaFile2);
                                cfg2.set("arena.lobbyLocation.world", p.getLocation().getWorld().getName());
                                cfg2.set("arena.lobbyLocation.x", p.getLocation().getX());
                                cfg2.set("arena.lobbyLocation.y", p.getLocation().getY());
                                cfg2.set("arena.lobbyLocation.z", p.getLocation().getZ());
                                cfg2.set("arena.lobbyLocation.pitch", p.getLocation().getPitch());
                                cfg2.set("arena.lobbyLocation.yaw", p.getLocation().getYaw());
                                try {
                                    cfg2.save(arenaFile2);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena2.addSpawnPoint(p.getLocation(), "lobby");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "sewer1":
                                Arena arena3 = this.editors.get(p.getUniqueId());
                                File arenaFile3 = arena3.getArenaFile();
                                YamlConfiguration cfg3 = YamlConfiguration.loadConfiguration(arenaFile3);
                                cfg3.set("arena.sewer1.world", p.getLocation().getWorld().getName());
                                cfg3.set("arena.sewer1.x", p.getLocation().getX());
                                cfg3.set("arena.sewer1.y", p.getLocation().getY());
                                cfg3.set("arena.sewer1.z", p.getLocation().getZ());
                                cfg3.set("arena.sewer1.pitch", p.getLocation().getPitch());
                                cfg3.set("arena.sewer1.yaw", p.getLocation().getYaw());
                                try {
                                    cfg3.save(arenaFile3);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena3.addSpawnPoint(p.getLocation(), "sewer1");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "sewer2":
                                Arena arena4 = this.editors.get(p.getUniqueId());
                                File arenaFile4 = arena4.getArenaFile();
                                YamlConfiguration cfg4 = YamlConfiguration.loadConfiguration(arenaFile4);
                                cfg4.set("arena.sewer2.world", p.getLocation().getWorld().getName());
                                cfg4.set("arena.sewer2.x", p.getLocation().getX());
                                cfg4.set("arena.sewer2.y", p.getLocation().getY());
                                cfg4.set("arena.sewer2.z", p.getLocation().getZ());
                                cfg4.set("arena.sewer2.pitch", p.getLocation().getPitch());
                                cfg4.set("arena.sewer2.yaw", p.getLocation().getYaw());
                                try {
                                    cfg4.save(arenaFile4);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena4.addSpawnPoint(p.getLocation(), "sewer2");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "entrance":
                                Arena arena5 = this.editors.get(p.getUniqueId());
                                File arenaFile5 = arena5.getArenaFile();
                                YamlConfiguration cfg5 = YamlConfiguration.loadConfiguration(arenaFile5);
                                cfg5.set("arena.entrance.world", p.getLocation().getWorld().getName());
                                cfg5.set("arena.entrance.x", p.getLocation().getX());
                                cfg5.set("arena.entrance.y", p.getLocation().getY());
                                cfg5.set("arena.entrance.z", p.getLocation().getZ());
                                cfg5.set("arena.entrance.pitch", p.getLocation().getPitch());
                                cfg5.set("arena.entrance.yaw", p.getLocation().getYaw());
                                try {
                                    cfg5.save(arenaFile5);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena5.addSpawnPoint(p.getLocation(), "entrance");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "floor1":
                                Arena arena6 = this.editors.get(p.getUniqueId());
                                File arenaFile6 = arena6.getArenaFile();
                                YamlConfiguration cfg6 = YamlConfiguration.loadConfiguration(arenaFile6);
                                cfg6.set("arena.floor1.world", p.getLocation().getWorld().getName());
                                cfg6.set("arena.floor1.x", p.getLocation().getX());
                                cfg6.set("arena.floor1.y", p.getLocation().getY());
                                cfg6.set("arena.floor1.z", p.getLocation().getZ());
                                cfg6.set("arena.floor1.pitch", p.getLocation().getPitch());
                                cfg6.set("arena.floor1.yaw", p.getLocation().getYaw());
                                try {
                                    cfg6.save(arenaFile6);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena6.addSpawnPoint(p.getLocation(), "floor1");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "floor2":
                                Arena arena7 = this.editors.get(p.getUniqueId());
                                File arenaFile7 = arena7.getArenaFile();
                                YamlConfiguration cfg7 = YamlConfiguration.loadConfiguration(arenaFile7);
                                cfg7.set("arena.floor2.world", p.getLocation().getWorld().getName());
                                cfg7.set("arena.floor2.x", p.getLocation().getX());
                                cfg7.set("arena.floor2.y", p.getLocation().getY());
                                cfg7.set("arena.floor2.z", p.getLocation().getZ());
                                cfg7.set("arena.floor2.pitch", p.getLocation().getPitch());
                                cfg7.set("arena.floor2.yaw", p.getLocation().getYaw());
                                try {
                                    cfg7.save(arenaFile7);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena7.addSpawnPoint(p.getLocation(), "floor2");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "orb":
                                Arena arena8 = this.editors.get(p.getUniqueId());
                                File arenaFile8 = arena8.getArenaFile();
                                YamlConfiguration cfg8 = YamlConfiguration.loadConfiguration(arenaFile8);
                                cfg8.set("arena.orb.world", p.getLocation().getWorld().getName());
                                cfg8.set("arena.orb.x", p.getLocation().getX());
                                cfg8.set("arena.orb.y", p.getLocation().getY());
                                cfg8.set("arena.orb.z", p.getLocation().getZ());
                                cfg8.set("arena.orb.pitch", p.getLocation().getPitch());
                                cfg8.set("arena.orb.yaw", p.getLocation().getYaw());
                                try {
                                    cfg8.save(arenaFile8);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena8.addSpawnPoint(p.getLocation(), "orb");
                                p.sendMessage(Lang.DEFENDER_SPAWNPOINT_SET.toString());
                            case "hub":
                                File hubFile = new File("plugins/TowerDefense/gameHub.yml");
                                if (!hubFile.exists()) {
                                    p.sendMessage(Lang.SETUP_HUB_BEFORE_SPAWNPOINT.toString());
                                    return true;
                                }
                                YamlConfiguration hCfg = YamlConfiguration.loadConfiguration(hubFile);
                                hCfg.set("hub.spawnLocation.world", p.getLocation().getWorld().getName());
                                hCfg.set("hub.spawnLocation.x", p.getLocation().getX());
                                hCfg.set("hub.spawnLocation.y", p.getLocation().getY());
                                hCfg.set("hub.spawnLocation.z", p.getLocation().getZ());
                                hCfg.set("hub.spawnLocation.pitch", p.getLocation().getPitch());
                                hCfg.set("hub.spawnLocation.yaw", p.getLocation().getYaw());
                                try {
                                    hCfg.save(hubFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                p.sendMessage(Lang.GAMEHUB_SPAWNPOINT_SET.toString());
                                return true;
                        }
                    case "sethub":
                        World world = Bukkit.getWorld(args[1]);
                        if (world == null) {
                            p.sendMessage(Lang.WORLD_NOT_FOUND.toString());
                            return true;
                        }
                        if (plugin.arenaHandler.arenaAlreadyExistsWorld(world)) {
                            p.sendMessage(Lang.CANT_SET_HUB_TO_ARENA_WORLD.toString());
                            return true;
                        }
                        File hubFile = new File("plugins/TowerDefense/gameHub.yml");
                        if (!hubFile.exists()) {
                            try {
                                hubFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(hubFile);
                        cfg.set("hub.world", world.getName());
                        try {
                            cfg.save(hubFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        p.sendMessage(Lang.GAMEHUB_SET.toString().replace("%world%", world.getName()));
                        return true;
                    default:
                        sendHelp(p);
                        return true;
                }
            case 3:
            case 4:
            case 5:
                switch (args[0]) {
                    case "arena":
                        switch (args[1]) {
                            case "create":
                                p.sendMessage(Lang.INSUFFICIENT_ARGS_TD_ARENA.toString().replace("%amount%", String.valueOf(args.length)));
                                p.sendMessage(Lang.COMMAND_TD_ARENA_USAGE.toString());
                                return true;
                            case "remove":
                                if (plugin.getArenaHandler().getArenaByName(args[2]) == null) {
                                    p.sendMessage(Lang.ARENA_NOT_FOUND.toString());
                                    return true;
                                }
                                Arena removeArena = TowerDefense.getInstance().getArenaHandler().getArenaByName(args[2]);
                                if (!removeArena.getPlayers().isEmpty()) {
                                    for (UUID uuid : removeArena.getPlayers()) {
                                        Player player = Bukkit.getPlayer(uuid);
                                        player.performCommand("leave");
                                        player.sendMessage("§6➢ §fYou have been removed from your previous arena because an admin initiated arena removal.");
                                    }
                                }
                                File removeFile = removeArena.getArenaFile();
                                removeFile.delete();
                                Util.log(Util.LogType.INFO, "Deleted file " + removeFile.getName() + " from arenaData as the arena was removed by an admin.");
                                plugin.getArenaHandler().removeArena(removeArena);
                                Bukkit.unloadWorld(removeArena.getWorld().getName(), true);
                                p.sendMessage(Lang.ARENA_SUCCESSFULLY_DELETED.toString().replace("%arena%", removeArena.getName()));
                                return true;
                            case "info":
                                showInfo(p, plugin.arenaHandler.getArenaByName(args[2]));
                                return true;
                            case "list":
                                listArenas(p);
                                return true;
                            case "edit":
                                Arena arena = plugin.arenaHandler.getArenaByName(args[2]);
                                if (arena == null) {
                                    p.sendMessage("§cNo such arena found! Please re-enter the arena name.");
                                    return true;
                                }
                                if (arena.getState().equals(GameState.CLEANUP) || arena.getState().equals(GameState.IN_PROGRESS)) {
                                    p.sendMessage("§cThis arena can't be edited now. (Arena state is: " + arena.getState().toString() + ")");
                                    return true;
                                }
                                if (arena.getState().equals(GameState.IN_EDITING) && editors.get(p.getUniqueId()) != arena) {
                                    p.sendMessage("§cSomeone else is already editing this arena!");
                                    return true;
                                }
                                if (isInGame(p)) {
                                    p.sendMessage(Lang.ALREADY_IN_GAME_EDIT.toString());
                                    return true;
                                }
                                else {
                                    if (arena.getPlayers().size() != 0)
                                        for (UUID uuid : arena.getPlayers()) {
                                            Player player = Bukkit.getPlayer(uuid);
                                            player.performCommand("leave");
                                            player.sendMessage("§6➢ §fYou have been removed from your previous arena because an admin started editing it.");
                                        }
                                    switchEdit(p, arena);
                                }
                                return true;
                            default:
                                sendHelp(p);
                                return true;
                        }
                    case "setspawnpoint":
                        if (!this.editors.containsKey(p.getUniqueId()) && !args[1].equalsIgnoreCase("hub")) {
                            p.sendMessage(Lang.NOT_EDITING_ARENA.toString());
                            return true;
                        }
                        switch (args[1]) {
                            case "attacker":
                                Arena arena = this.editors.get(p.getUniqueId());
                                File arenaFile = arena.getArenaFile();
                                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(arenaFile);
                                cfg.set("arena.attackerLocation.world", p.getLocation().getWorld().getName());
                                cfg.set("arena.attackerLocation.x", p.getLocation().getX());
                                cfg.set("arena.attackerLocation.y", p.getLocation().getY());
                                cfg.set("arena.attackerLocation.z", p.getLocation().getZ());
                                cfg.set("arena.attackerLocation.pitch", p.getLocation().getPitch());
                                cfg.set("arena.attackerLocation.yaw", p.getLocation().getYaw());
                                try {
                                    cfg.save(arenaFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena.addSpawnPoint(p.getLocation(), "attacker");
                                p.sendMessage(Lang.ATTACKER_SPAWNPOINT_SET.toString());
                                return true;
                            case "defender":
                                Arena arena1 = this.editors.get(p.getUniqueId());
                                File arenaFile1 = arena1.getArenaFile();
                                YamlConfiguration cfg1 = YamlConfiguration.loadConfiguration(arenaFile1);
                                cfg1.set("arena.defenderLocation.world", p.getLocation().getWorld().getName());
                                cfg1.set("arena.defenderLocation.x", p.getLocation().getX());
                                cfg1.set("arena.defenderLocation.y", p.getLocation().getY());
                                cfg1.set("arena.defenderLocation.z", p.getLocation().getZ());
                                cfg1.set("arena.defenderLocation.pitch", p.getLocation().getPitch());
                                cfg1.set("arena.defenderLocation.yaw", p.getLocation().getYaw());
                                try {
                                    cfg1.save(arenaFile1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena1.addSpawnPoint(p.getLocation(), "defender");
                                p.sendMessage(Lang.DEFENDER_SPAWNPOINT_SET.toString());
                                return true;
                            case "lobby":
                                Arena arena2 = this.editors.get(p.getUniqueId());
                                File arenaFile2 = arena2.getArenaFile();
                                YamlConfiguration cfg2 = YamlConfiguration.loadConfiguration(arenaFile2);
                                cfg2.set("arena.lobbyLocation.world", p.getLocation().getWorld().getName());
                                cfg2.set("arena.lobbyLocation.x", p.getLocation().getX());
                                cfg2.set("arena.lobbyLocation.y", p.getLocation().getY());
                                cfg2.set("arena.lobbyLocation.z", p.getLocation().getZ());
                                cfg2.set("arena.lobbyLocation.pitch", p.getLocation().getPitch());
                                cfg2.set("arena.lobbyLocation.yaw", p.getLocation().getYaw());
                                try {
                                    cfg2.save(arenaFile2);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena2.addSpawnPoint(p.getLocation(), "lobby");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "sewer1":
                                Arena arena3 = this.editors.get(p.getUniqueId());
                                File arenaFile3 = arena3.getArenaFile();
                                YamlConfiguration cfg3 = YamlConfiguration.loadConfiguration(arenaFile3);
                                cfg3.set("arena.sewer1.world", p.getLocation().getWorld().getName());
                                cfg3.set("arena.sewer1.x", p.getLocation().getX());
                                cfg3.set("arena.sewer1.y", p.getLocation().getY());
                                cfg3.set("arena.sewer1.z", p.getLocation().getZ());
                                cfg3.set("arena.sewer1.pitch", p.getLocation().getPitch());
                                cfg3.set("arena.sewer1.yaw", p.getLocation().getYaw());
                                try {
                                    cfg3.save(arenaFile3);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena3.addSpawnPoint(p.getLocation(), "sewer1");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "sewer2":
                                Arena arena4 = this.editors.get(p.getUniqueId());
                                File arenaFile4 = arena4.getArenaFile();
                                YamlConfiguration cfg4 = YamlConfiguration.loadConfiguration(arenaFile4);
                                cfg4.set("arena.sewer2.world", p.getLocation().getWorld().getName());
                                cfg4.set("arena.sewer2.x", p.getLocation().getX());
                                cfg4.set("arena.sewer2.y", p.getLocation().getY());
                                cfg4.set("arena.sewer2.z", p.getLocation().getZ());
                                cfg4.set("arena.sewer2.pitch", p.getLocation().getPitch());
                                cfg4.set("arena.sewer2.yaw", p.getLocation().getYaw());
                                try {
                                    cfg4.save(arenaFile4);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena4.addSpawnPoint(p.getLocation(), "sewer2");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "entrance":
                                Arena arena5 = this.editors.get(p.getUniqueId());
                                File arenaFile5 = arena5.getArenaFile();
                                YamlConfiguration cfg5 = YamlConfiguration.loadConfiguration(arenaFile5);
                                cfg5.set("arena.entrance.world", p.getLocation().getWorld().getName());
                                cfg5.set("arena.entrance.x", p.getLocation().getX());
                                cfg5.set("arena.entrance.y", p.getLocation().getY());
                                cfg5.set("arena.entrance.z", p.getLocation().getZ());
                                cfg5.set("arena.entrance.pitch", p.getLocation().getPitch());
                                cfg5.set("arena.entrance.yaw", p.getLocation().getYaw());
                                try {
                                    cfg5.save(arenaFile5);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena5.addSpawnPoint(p.getLocation(), "entrance");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "floor1":
                                Arena arena6 = this.editors.get(p.getUniqueId());
                                File arenaFile6 = arena6.getArenaFile();
                                YamlConfiguration cfg6 = YamlConfiguration.loadConfiguration(arenaFile6);
                                cfg6.set("arena.floor1.world", p.getLocation().getWorld().getName());
                                cfg6.set("arena.floor1.x", p.getLocation().getX());
                                cfg6.set("arena.floor1.y", p.getLocation().getY());
                                cfg6.set("arena.floor1.z", p.getLocation().getZ());
                                cfg6.set("arena.floor1.pitch", p.getLocation().getPitch());
                                cfg6.set("arena.floor1.yaw", p.getLocation().getYaw());
                                try {
                                    cfg6.save(arenaFile6);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena6.addSpawnPoint(p.getLocation(), "floor1");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "floor2":
                                Arena arena7 = this.editors.get(p.getUniqueId());
                                File arenaFile7 = arena7.getArenaFile();
                                YamlConfiguration cfg7 = YamlConfiguration.loadConfiguration(arenaFile7);
                                cfg7.set("arena.floor2.world", p.getLocation().getWorld().getName());
                                cfg7.set("arena.floor2.x", p.getLocation().getX());
                                cfg7.set("arena.floor2.y", p.getLocation().getY());
                                cfg7.set("arena.floor2.z", p.getLocation().getZ());
                                cfg7.set("arena.floor2.pitch", p.getLocation().getPitch());
                                cfg7.set("arena.floor2.yaw", p.getLocation().getYaw());
                                try {
                                    cfg7.save(arenaFile7);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena7.addSpawnPoint(p.getLocation(), "floor2");
                                p.sendMessage(Lang.LOBBY_SPAWNPOINT_SET.toString());
                                return true;
                            case "orb":
                                Arena arena8 = this.editors.get(p.getUniqueId());
                                File arenaFile8 = arena8.getArenaFile();
                                YamlConfiguration cfg8 = YamlConfiguration.loadConfiguration(arenaFile8);
                                cfg8.set("arena.orb.world", p.getLocation().getWorld().getName());
                                cfg8.set("arena.orb.x", p.getLocation().getX());
                                cfg8.set("arena.orb.y", p.getLocation().getY());
                                cfg8.set("arena.orb.z", p.getLocation().getZ());
                                cfg8.set("arena.orb.pitch", p.getLocation().getPitch());
                                cfg8.set("arena.orb.yaw", p.getLocation().getYaw());
                                try {
                                    cfg8.save(arenaFile8);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                arena8.addSpawnPoint(p.getLocation(), "orb");
                                p.sendMessage(Lang.DEFENDER_SPAWNPOINT_SET.toString());
                            case "hub":
                                File hubFile = new File("plugins/TowerDefense/gameHub.yml");
                                if (!hubFile.exists()) {
                                    p.sendMessage(Lang.SETUP_HUB_BEFORE_SPAWNPOINT.toString());
                                    return true;
                                }
                                YamlConfiguration hCfg = YamlConfiguration.loadConfiguration(hubFile);
                                hCfg.set("hub.spawnLocation.world", p.getLocation().getWorld().getName());
                                hCfg.set("hub.spawnLocation.x", p.getLocation().getX());
                                hCfg.set("hub.spawnLocation.y", p.getLocation().getY());
                                hCfg.set("hub.spawnLocation.z", p.getLocation().getZ());
                                hCfg.set("hub.spawnLocation.pitch", p.getLocation().getPitch());
                                hCfg.set("hub.spawnLocation.yaw", p.getLocation().getYaw());
                                try {
                                    hCfg.save(hubFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                p.sendMessage(Lang.GAMEHUB_SPAWNPOINT_SET.toString());
                                return true;
                        }
                    case "sethub":
                        World world = Bukkit.getWorld(args[1]);
                        if (world == null) {
                            p.sendMessage(Lang.WORLD_NOT_FOUND.toString());
                            return true;
                        }
                        if (plugin.arenaHandler.arenaAlreadyExistsWorld(world)) {
                            p.sendMessage(Lang.CANT_SET_HUB_TO_ARENA_WORLD.toString());
                            return true;
                        }
                        File hubFile = new File("plugins/TowerDefense/gameHub.yml");
                        if (!hubFile.exists()) {
                            try {
                                hubFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(hubFile);
                        cfg.set("hub.world", world.getName());
                        try {
                            cfg.save(hubFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        p.sendMessage(Lang.GAMEHUB_SET.toString().replace("%world%", world.getName()));
                        return true;
                    default:
                        sendHelp(p);
                        return true;
                }
            case 6:
            default:
                switch (args[0]) {
                    case "arena":
                        switch (args[1]) {
                            case "create":
                                String name = args[2];
                                if (plugin.arenaHandler.getArenaByName(name) != null) {
                                    p.sendMessage("§c§l(!) §fAn arena with this name already exists.");
                                    return true;
                                }
                                if (!Util.isNumber(args[3])) {
                                    p.sendMessage(Lang.COMMAND_TD_ARENA_NOT_NUMBER.toString().replace("%arg%", args[3]));
                                    return true;
                                }
                                Integer id = Integer.parseInt(args[3]);
                                if (plugin.arenaHandler.getArenaById(id) != null) {
                                    p.sendMessage("§c§l(!) §fAn arena with this ID already exists.");
                                    return true;
                                }
                                World world = Bukkit.getWorld(args[4]);
                                if (world == null) {
                                    Bukkit.createWorld(new WorldCreator(args[4]).type(WorldType.FLAT));
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import " + args[4] + " NORMAL");
                                    world = Bukkit.getWorld(args[4]);
                                    p.sendMessage(Lang.COMMAND_TD_ARENA_CREATE_WORLD_NOT_FOUND.toString());
                                }
                                if (plugin.arenaHandler.getArenaByWorld(world.getName()) != null) {
                                    p.sendMessage("§c§l(!) §fAn arena with this world already exists.");
                                    return true;
                                }
                                File hubFile = new File("plugins/TowerDefense/gameHub.yml");
                                if (hubFile.exists()) {
                                    if (YamlConfiguration.loadConfiguration(hubFile).get("hub.world").equals(world.getName())) {
                                        p.sendMessage(Lang.CANT_SET_ARENA_TO_HUB_WORLD.toString());
                                        return true;
                                    }
                                }
                                if (!Util.isNumber(args[5])) {
                                    p.sendMessage(Lang.COMMAND_TD_ARENA_NOT_NUMBER.toString().replace("%arg%", args[5]));
                                    return true;
                                }
                                Integer timeLimit = Integer.parseInt(args[5]);
                                p.sendMessage("§c§l(!) §fCreating arena. This process might take a while, please be patient.");
                                try {
                                    createArena(name, id, world.getName(), timeLimit);
                                    Util.copyFileStructure(world.getWorldFolder(), new File(Bukkit.getWorldContainer(), "copy_" + world.getName()));
                                } catch (IOException e) {
                                    Util.log(Util.LogType.ERROR, "Error occured while trying to setup an arena. (IOEXCEPTION)");
                                }
                                TextComponent message = new TextComponent("§6➢ §fClick §n§e§lHERE §fto teleport to world " + world.getName() + "!");
                                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oClick here to start editing this arena!").create()));
                                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/td arena edit " + name));
                                p.spigot().sendMessage(message);
                                return true;
                            case "remove":
                                if (plugin.getArenaHandler().getArenaByName(args[2]) == null) {
                                    p.sendMessage(Lang.ARENA_NOT_FOUND.toString());
                                    return true;
                                }
                                Arena removeArena = TowerDefense.getInstance().getArenaHandler().getArenaByName(args[2]);
                                File removeFile = removeArena.getArenaFile();
                                removeFile.delete();
                                Util.log(Util.LogType.INFO, "Deleted file " + removeFile.getName() + " from arenaData as the arena was removed by an admin.");
                                plugin.getArenaHandler().removeArena(removeArena);
                                Bukkit.unloadWorld(removeArena.getWorld().getName(), true);
                                p.sendMessage(Lang.ARENA_SUCCESSFULLY_DELETED.toString().replace("%arena%", removeArena.getName()));
                                return true;
                            case "info":
                                showInfo(p, plugin.arenaHandler.getArenaByName(args[2]));
                                return true;
                            case "list":
                                listArenas(p);
                                return true;
                            case "edit":
                                Arena arena = plugin.arenaHandler.getArenaByName(args[2]);
                                if (arena == null) {
                                    p.sendMessage("§cNo such arena found! Please re-enter the arena name.");
                                    return true;
                                }
                                if (arena.getState().equals(GameState.CLEANUP) || arena.getState().equals(GameState.IN_PROGRESS)) {
                                    p.sendMessage("§cThis arena can't be edited now. (Arena state is: " + arena.getState().toString() + ")");
                                    return true;
                                }
                                if (arena.getState().equals(GameState.IN_EDITING) && editors.get(p.getUniqueId()) != arena) {
                                    p.sendMessage("§cSomeone else is already editing this arena!");
                                    return true;
                                }
                                else {
                                    if (arena.getPlayers().size() != 0)
                                        for (UUID uuid : arena.getPlayers()) {
                                            Player player = Bukkit.getPlayer(uuid);
                                            arena.removePlayer(player);
                                            player.sendMessage("§6➢ §fYou have been removed from your previous arena because an admin started editing it.");
                                            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                                        }
                                    switchEdit(p, arena);
                                }
                                return true;
                            default:
                                sendHelp(p);
                                return true;
                        }
                    default:
                        sendHelp(p);
                        return true;
                }
        }
    }
}
