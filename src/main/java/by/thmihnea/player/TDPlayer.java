package by.thmihnea.player;

import by.thmihnea.TowerDefense;
import by.thmihnea.runnables.Cooldown;
import by.thmihnea.runnables.CooldownType;
import org.bukkit.entity.Player;

import java.util.*;

public class TDPlayer {

    public static Map<UUID, TDPlayer> tdPlayers = new HashMap<>();

    private boolean editing;
    private boolean inGame;
    private boolean bypassBuild;
    private String lastTeam;
    private Player player;
    private List<Cooldown> cooldowns;
    private Integer money;
    private Integer deaths;
    private Integer kills;

    public TDPlayer(Player player) {
        this.cooldowns = new ArrayList<Cooldown>();
        this.editing = false;
        this.inGame = false;
        this.bypassBuild = false;
        this.player = player;
        this.kills = 0;
        this.deaths = 0;
        this.money = TowerDefense.cfg.getInt("bounty.default_money_on_kill");
    }

    public void resetMoney() {
        this.money = TowerDefense.cfg.getInt("bounty.default_money_on_kill");
    }

    public void addKill() {
        this.kills++;
    }

    public void addDeath() {
        this.deaths++;
    }

    public void setupBountyOnKill() {
        this.money = this.money + (30 * this.kills) - (30 * this.deaths);
        if (this.money <= 30) this.money = 30;
        if (this.money >= 800) this.money = 800;
    }

    public void resetKills() {
        this.kills = 0;
    }

    public void resetDeaths() {
        this.deaths = 0;
    }

    public Integer getDeaths() {
        return this.deaths;
    }

    public Integer getKills() {
        return this.kills;
    }

    public Integer getMoney() {
        return this.money;
    }

    public List<Cooldown> getCooldowns() {
        return this.cooldowns;
    }

    public boolean hasCooldown(CooldownType cooldownType) {
        for (Cooldown cooldown : this.cooldowns)
            if (cooldown.getCooldownType().equals(cooldownType)) return true;
        return false;
    }

    public Cooldown getCooldownByType(CooldownType cooldownType) {
        for (Cooldown cooldown : this.cooldowns)
            if (cooldown.getCooldownType().equals(cooldownType))
                return cooldown;
        return null;
    }

    public void addCooldown(Cooldown cooldown) {
        this.cooldowns.add(cooldown);
    }

    public void removeCooldown(Cooldown cooldown) {
        this.cooldowns.remove(cooldown);
    }

    public void setLastTeam(String lastTeam) {
        this.lastTeam = lastTeam;
    }

    public boolean canBypassBuild() {
        return this.bypassBuild;
    }

    public void setCanBypassBuild(boolean bypassBuild) {
        this.bypassBuild = bypassBuild;
    }

    public String getLastTeam() {
        return this.lastTeam;
    }

    public boolean isInGame() {
        return this.inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isEditing() {
        return this.editing;
    }

    public void setEditing(boolean isEditing) {
        this.editing = isEditing;
    }
}
