package by.thmihnea.arena;

import by.thmihnea.TowerDefense;
import by.thmihnea.Util;
import by.thmihnea.events.ArenaSwitchStateEvent;
import by.thmihnea.events.PlayerJoinArenaEvent;
import by.thmihnea.events.PlayerLeaveArenaEvent;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.runnables.*;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.util.item.ArmorUtil;
import by.thmihnea.util.item.SelectorUtil;
import by.thmihnea.util.item.ShopItemUtil;
import com.connorlinfoot.bountifulapi.BountifulAPI;
import com.google.common.base.Predicate;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class Arena {

    private List<UUID> players;
    private Map<String, Location> spawnPoints;
    private List<UUID> attackers;
    private List<UUID> defenders;
    private List<Block> traps;
    private Map<UUID, Integer> money;
    private Integer id;
    private String name;
    private World world;
    private Integer timeLimit;
    private Integer orbBreak;
    private File arenaFile;
    private GameState gameState;
    private Countdown countdown;
    private GameTimer gameTimer;
    private PostgameTimer postgameTimer;
    private Particles particles;

    public Arena(Integer id, String name, World world,
                 Integer timeLimit, File arenaFile) {
        players = new ArrayList<UUID>();
        attackers = new ArrayList<UUID>();
        defenders = new ArrayList<UUID>();
        traps = new ArrayList<Block>();
        money = new HashMap<UUID, Integer>();
        this.id = id;
        this.name = name;
        this.world = world;
        this.timeLimit = timeLimit;
        this.orbBreak = 0;
        this.arenaFile = arenaFile;
        spawnPoints = new HashMap<String, Location>() {
            {
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(arenaFile);
                List<String> points = Arrays.asList("attackerLocation", "defenderLocation", "lobbyLocation", "sewer1", "sewer2", "entrance", "floor1", "floor2", "orb");
                points.stream().forEach(s -> {
                    if (cfg.contains("arena." + s)) {
                        World world = getWorld();
                        double x = cfg.getDouble("arena." + s + ".x");
                        double y = cfg.getDouble("arena." + s + ".y");
                        double z = cfg.getDouble("arena." + s + ".z");
                        float pitch = (float) cfg.getDouble("arena." + s + ".pitch");
                        float yaw = (float) cfg.getDouble("arena." + s + ".yaw");
                        Location location = new Location(world, x, y, z, pitch, yaw);
                        put(s.replace("Location", ""), location);
                    }
                });
            }
        };
        if (spawnPoints.size() < 9) this.gameState = GameState.NEEDS_SETUP;
        else this.gameState = GameState.WAITING;
    }

    private void teamSwitch(int k, Player p) {
        TDPlayer tdp = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (k == 1) {
            this.defenders.add(p.getUniqueId());
            tdp.setLastTeam("defender");
        } else {
            this.attackers.add(p.getUniqueId());
            tdp.setLastTeam("attacker");
        }
    }

    private int switchK(int k) {
        if (k == 0) return 1;
        else return 0;
    }

    public void giveTeamMoney() {
        for (UUID uuid : this.players) {
            Player p = Bukkit.getPlayer(uuid);
            int amount;
            if (isAttacker(p)) amount = TowerDefense.cfg.getInt("income.attacker");
            else amount = TowerDefense.cfg.getInt("income.defender");
            setMoney(p, getMoney(p) + amount);
        }
    }

    private void setupTeams() {
        ArrayList<UUID> copy = new ArrayList<UUID>(this.players.size());
        for (UUID uuid : this.players)
            copy.add(uuid);
        Random random = new Random();
        int k = 0;
        for (int i = 0; i < players.size(); i++) {
            int randomIndex = random.nextInt(copy.size());
            UUID uuid = copy.get(randomIndex);
            Player p = Bukkit.getPlayer(uuid);
            teamSwitch(k, p);
            k = switchK(k);
            copy.remove(randomIndex);
        }
    }

    public List<UUID> getAttackers() {
        return this.attackers;
    }

    public List<UUID> getDefenders() {
        return this.defenders;
    }

    public Map<UUID, Integer> getEconomy() {
        return this.money;
    }

    public void removeFromEcon(Player player) {
        this.money.remove(player.getUniqueId());
    }

    public void setMoney(Player player, Integer amount) {
        this.money.put(player.getUniqueId(), amount);
    }

    public int getMoney(Player player) {
        if (!this.money.containsKey(player.getUniqueId())) return 0;
        return this.money.get(player.getUniqueId());
    }

    public boolean isAttacker(Player p) {
        return this.attackers.contains(p.getUniqueId());
    }

    public boolean isDefender(Player p) {
        return this.defenders.contains(p.getUniqueId());
    }

    public boolean isSameTeam(Player p1, Player p2) {
        if ((isAttacker(p1) && isAttacker(p2)) || isDefender(p1) && isDefender(p2)) return true;
        else return false;
    }

    public void startGame() {
        Location attackerSpawn = spawnPoints.get("attacker");
        Location defenderSpawn = spawnPoints.get("defender");
        setupTeams();
        this.setState(GameState.IN_PROGRESS);
        broadCastMessage(Lang.GAME_STARTED.toString());
        Location location = spawnPoints.get("orb");
        Block block = location.getBlock();
        block.setType(XMaterial.GLASS.parseMaterial());
        for (UUID uuid : this.players) {
            Player p = Bukkit.getPlayer(uuid);
            TDPlayer tdp = TDPlayer.tdPlayers.get(p.getUniqueId());
            if (isAttacker(p)) {
                p.teleport(attackerSpawn);
                ArmorUtil.setupAttackerItems(p);
            } else {
                p.teleport(defenderSpawn);
                ArmorUtil.setupDefenderItems(p);
            }
            ShopItemUtil.giveShop(p);
            this.money.put(p.getUniqueId(), 0);
            tdp.setInGame(true);
        }
        GameTimer gameTimer = new GameTimer(this, this.timeLimit * 60);
        this.setGameTimer(gameTimer);
        this.particles = new Particles(this);
    }

    public void resetArena() {
        this.particles.clear();
        this.particles = null;
        this.orbBreak = 0;
        emptyPlayers();
        emptyTraps();
        cleanupWorld();
    }

    public Integer getOrbBreak() {
        return this.orbBreak;
    }

    public void setOrbBreak(Integer number) {
        this.orbBreak = number;
    }

    public void cleanupWorld() {
        String worldName = this.getWorld().getName();
        String path = this.getWorld().getWorldFolder().getPath();
        String worldContainer = Bukkit.getWorldContainer().getAbsolutePath();
        for (Entity e : this.getWorld().getEntities()) e.remove();
        Util.log(Util.LogType.INFO, "Removing all entities for safe deletion of world " + worldName);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload " + worldName);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload copy_" + worldName);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv delete " + this.getWorld().getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvconfirm");
        Util.log(Util.LogType.INFO, "World " + worldName + " has been deleted. Reinitializing a cached copy of it...");
        File newWorldFile = new File(path);
        newWorldFile.mkdir();
        Util.copyFileStructure(new File(worldContainer, "copy_" + worldName), newWorldFile);
        Util.log(Util.LogType.INFO, "Copied all files necessarry for world " + worldName + ". Loading world...");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import " + worldName + " NORMAL");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv load " + worldName);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv load copy_" + worldName);
        final World newWorld = Bukkit.getWorld(worldName);
        world.setAutoSave(false);
        world.setAnimalSpawnLimit(0);
        world.setTime(0L);
        world.setStorm(false);
        world.setThundering(false);
        this.setWorld(newWorld);
        this.resetSpawnpoints();
        this.setState(GameState.WAITING);
        Util.log(Util.LogType.INFO, "World " + worldName + " has been successfully loaded. Arena " + this.getName() + "| ID " + this.getId() + " has been successfully cleaned up. Changing state to WAITING...");
    }

    public void resetSpawnpoints() {
        for (int i = 1; i <= spawnPoints.size(); i++)
            removeSpawnPoint(spawnPoints.get(0));
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(arenaFile);
        List<String> points = Arrays.asList("attackerLocation", "defenderLocation", "lobbyLocation", "sewer1", "sewer2", "entrance", "floor1", "floor2", "orb");
        points.stream().forEach(s -> {
            if (cfg.contains("arena." + s)) {
                World world = getWorld();
                double x = cfg.getDouble("arena." + s + ".x");
                double y = cfg.getDouble("arena." + s + ".y");
                double z = cfg.getDouble("arena." + s + ".z");
                float pitch = (float) cfg.getDouble("arena." + s + ".pitch");
                float yaw = (float) cfg.getDouble("arena." + s + ".yaw");
                Location location = new Location(world, x, y, z, pitch, yaw);
                spawnPoints.put(s.replace("Location", ""), location);
            }
        });
    }

    public void emptyTraps() {
        for (Block block : new ArrayList<Block>(this.traps))
            traps.remove(block);
    }

    public void addTrap(Block block) {
        this.traps.add(block);
    }

    public List<Block> getTraps() {
        return this.traps;
    }

    public void removeTrap(Block block) {
        this.traps.remove(block);
    }

    public void broadCastMessage(String message) {
        if (this.players.isEmpty()) return;
        for (UUID uuid : this.players) {
            Player p = Bukkit.getPlayer(uuid);
            p.sendMessage(message);
        }
    }

    public void emptyPlayers() {
        File hubFile = new File("plugins/TowerDefense/gameHub.yml");;
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(hubFile);
        for (UUID uuid : new ArrayList<UUID>(this.players)) {
            if (this.players.isEmpty()) break;
            Player p = Bukkit.getPlayer(uuid);
            TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
            tdPlayer.setInGame(false);
            p.teleport(new Location(Bukkit.getWorld(cfg.getString("hub.spawnLocation.world")), cfg.getDouble("hub.spawnLocation.x"), cfg.getDouble("hub.spawnLocation.y"), cfg.getDouble("hub.spawnLocation.z"), (float) cfg.getDouble("hub.spawnLocation.pitch"), (float) cfg.getDouble("hub.spawnLocation.yaw")));
            p.setFoodLevel(20);
            p.setHealth(20.0D);
            p.getInventory().clear();
            p.getActivePotionEffects().clear();
            ArmorUtil.clearArmor(p);
            p.getInventory().setHelmet(null);
            SelectorUtil.giveSelector(p);
            removeFromTeam(p);
            removeFromEcon(p);
            removePlayer(p);
        }
    }

    public void broadCastActionBar(String message, boolean addObjective) {
        if (this.players.isEmpty()) return;
        for (UUID uuid : this.players) {
            Player p = Bukkit.getPlayer(uuid);
            String obj = "";
            String purse = "§fPurse: §e" + String.format("%,d", this.getMoney(p)) + " §8| ";
            if (addObjective == true) {
                if (isAttacker(p)) {
                    obj = "§6• §fObjective: §c§lDESTROY THE ORB §8| ";
                } else {
                    obj = "§6• §fObjective: §a§lDEFEND THE ORB §8| ";
                }
            }
            BountifulAPI.sendActionBar(p, obj + purse + message);
        }
    }

    public PostgameTimer getPostgameTimer() {
        return this.postgameTimer;
    }

    public void setPostgameTimer(PostgameTimer postgameTimer) {
        this.postgameTimer = postgameTimer;
    }

    public void stopPostgameTimer() {
        this.postgameTimer.clear();
    }

    public Countdown getCountdown() {
        return this.countdown;
    }

    public void setCountdown(Countdown countdown) {
        this.countdown = countdown;
    }

    public void stopCountdown() {
        this.countdown.cancel();
    }

    public GameTimer getGameTimer() {
        return this.gameTimer;
    }

    public void setGameTimer(GameTimer gameTimer) {
        this.gameTimer = gameTimer;
    }

    public void stopGameTimer() {
        getGameTimer().clear();
        if (this.getState() == GameState.IN_PROGRESS) {
            this.broadCastMessage(Lang.MATCH_ENDED.toString());
            this.setState(GameState.CLEANUP);
            PostgameTimer postgameTimer = new PostgameTimer(this, 30);
            this.setPostgameTimer(postgameTimer);
        }
    }

    public Map<String, Location> getSpawnPoints() {
        return this.spawnPoints;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Integer getTimeLimit() {
        return this.timeLimit;
    }

    public File getArenaFile() {
        return this.arenaFile;
    }

    public GameState getState() {
        return this.gameState;
    }

    public void setState(GameState gameState) {
        GameState previousState = this.gameState;
        this.gameState = gameState;
        ArenaSwitchStateEvent e = new ArenaSwitchStateEvent(this, previousState);
        Bukkit.getPluginManager().callEvent(e);
    }

    public void addSpawnPoint(Location location, String string) {
        if (!hasSpawnPoint(location))
            spawnPoints.put(string, location);
    }

    public void removeSpawnPoint(Location location) {
        if (hasSpawnPoint(location))
            spawnPoints.remove(location);
    }

    public void addPlayer(Player player) {
        if (!inArena(player))
            players.add(player.getUniqueId());
        System.out.println(players);
        PlayerJoinArenaEvent e = new PlayerJoinArenaEvent(this, player);
        Bukkit.getPluginManager().callEvent(e);
    }

    public void removePlayer(Player player) {
        if (inArena(player))
            players.remove(player.getUniqueId());
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(player.getUniqueId());
        if (!tdPlayer.getCooldowns().isEmpty())
            for (Cooldown cooldown : new ArrayList<Cooldown>(tdPlayer.getCooldowns())) {
                tdPlayer.removeCooldown(cooldown);
            }
        tdPlayer.resetMoney();
        tdPlayer.resetDeaths();
        tdPlayer.resetKills();
        PlayerLeaveArenaEvent e = new PlayerLeaveArenaEvent(this, player);
        Bukkit.getPluginManager().callEvent(e);
    }

    public void removeFromTeam(Player player) {
        if (isAttacker(player))
            attackers.remove(player.getUniqueId());
        if (isDefender(player))
            defenders.remove(player.getUniqueId());
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

    public boolean inArena(Player player) {
        return players.contains(player.getUniqueId());
    }

    public boolean hasSpawnPoint(Location location) {
        return spawnPoints.containsKey(location);
    }

    public boolean canDoAttack(EntityPlayer entityPlayer) {
        Player p = entityPlayer.getBukkitEntity();
        if (isAttacker(p)) return true;
        else return false;
    }
}
