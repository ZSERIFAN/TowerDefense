package by.thmihnea.listeners.hub;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.runnables.Cooldown;
import by.thmihnea.runnables.CooldownType;
import by.thmihnea.runnables.Respawn;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDamageListener implements Listener {

    public static void clearInventory(Player p) {
        for (ItemStack is : p.getInventory().getContents()) {
            if (is == null || is.getType() == Material.AIR) continue;
            if (!is.getType().toString().toUpperCase().contains("SWORD") &&
                    !is.getType().toString().toUpperCase().contains("HELMET") &&
                    !is.getType().toString().toUpperCase().contains("CHESTPLATE") &&
                    !is.getType().toString().toUpperCase().contains("LEGGINGS") &&
                    !is.getType().toString().toUpperCase().contains("BOOTS"))
                p.getInventory().remove(is);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        Entity ent = e.getEntity();
        Entity damager = e.getDamager();
        if (!(ent instanceof Player)) return;
        Player p = (Player) ent;
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        if (arena != null) {
            if (arena.getState().equals(GameState.CLEANUP)) e.setCancelled(true);
        }
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (!tdPlayer.isInGame()) {
            e.setCancelled(true);
            return;
        }
        if (tdPlayer.hasCooldown(CooldownType.COMBAT)) tdPlayer.getCooldownByType(CooldownType.COMBAT).resetTime();
        else {
            Cooldown cd = new Cooldown(arena, p, CooldownType.COMBAT, TowerDefense.cfg.getInt("cooldowns.combat"));
            tdPlayer.addCooldown(cd);
        }
        if (p.getHealth() - e.getFinalDamage() <= 0) {
            if (damager instanceof Player) {
                Player dmgr = (Player) damager;
                if ((arena.isAttacker(p) && arena.isAttacker(dmgr)) || (arena.isDefender(p) && arena.isDefender(dmgr))) {
                    return;
                }
            }
            p.setGameMode(GameMode.SPECTATOR);
            clearInventory(p);
            e.setDamage(0.001);
            p.setHealth(20.0);
            new Respawn(p, TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p), TowerDefense.cfg.getInt("timers.respawn_timer"));
            if (damager instanceof Player) {
                Player dmgr = (Player) damager;
                TDPlayer tdpdmgr = TDPlayer.tdPlayers.get(dmgr.getUniqueId());
                tdpdmgr.resetDeaths();
                tdpdmgr.addKill();
                tdpdmgr.setupBountyOnKill();
                dmgr.sendMessage(Lang.MONEY_ON_KILL.toString().replace("%money%", String.valueOf(tdPlayer.getMoney())).replace("%player%", p.getName()));
                arena.setMoney(dmgr, arena.getMoney(dmgr) + tdPlayer.getMoney());
                tdPlayer.resetKills();
                tdPlayer.addDeath();
                tdPlayer.resetMoney();
                tdPlayer.setupBountyOnKill();
                arena.broadCastMessage(Lang.ARENA_KILL_MESSAGE.toString().replace("%damaged%", p.getName()).replace("%damager%", dmgr.getName()));
            } else {
                switch (damager.getType()) {
                    case ZOMBIE:
                        arena.broadCastMessage(Lang.DIE_DUE_TO_ZOMBIE.toString().replace("%player%", p.getName()));
                        break;
                    case SKELETON:
                        arena.broadCastMessage(Lang.DIE_DUE_TO_SKELETON.toString().replace("%player%", p.getName()));
                        break;
                    case WOLF:
                        arena.broadCastMessage(Lang.DIE_DUE_TO_WOLF.toString().replace("%player%", p.getName()));
                        break;
                    case WITCH:
                        arena.broadCastMessage(Lang.DIE_DUE_TO_WITCH.toString().replace("%player%", p.getName()));
                        break;
                    case IRON_GOLEM:
                        arena.broadCastMessage(Lang.DIE_DUE_TO_GOLEM.toString().replace("%player%", p.getName()));
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        e.setDroppedExp(0);
    }

    @EventHandler
    public void onDamageSelf(EntityDamageEvent e) {
        if (e.isCancelled()) return;
        Entity ent = e.getEntity();
        if (!(ent instanceof Player)) return;
        if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
        Player p = (Player) ent;
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        if (arena != null) {
            if (arena.getState().equals(GameState.CLEANUP)) e.setCancelled(true);
        }
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (!tdPlayer.isInGame()) e.setCancelled(true);
        else {
            if (tdPlayer.hasCooldown(CooldownType.COMBAT)) tdPlayer.getCooldownByType(CooldownType.COMBAT).resetTime();
            else {
                Cooldown cd = new Cooldown(arena, p, CooldownType.COMBAT, TowerDefense.cfg.getInt("cooldowns.combat"));
                tdPlayer.addCooldown(cd);
            }
        }
        if (p.getHealth() - e.getFinalDamage() <= 0) {
            clearInventory(p);
            e.setDamage(0.01D);
            p.setGameMode(GameMode.SPECTATOR);
            p.setHealth(20.0D);
            new Respawn(p, TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p), TowerDefense.cfg.getInt("timers.respawn_timer"));
            switch (e.getCause()) {
                case FIRE:
                case FIRE_TICK:
                case LAVA:
                    arena.broadCastMessage(Lang.DIE_DUE_TO_LAVA.toString().replace("%player%", p.getName()));
                    return;
                case SUICIDE:
                case FALL:
                case VOID:
                    arena.broadCastMessage(Lang.DIE_DUE_TO_SUICIDE.toString().replace("%player%", p.getName()));
                    return;
                case DROWNING:
                    arena.broadCastMessage(Lang.DIE_DUE_TO_DROWNING.toString().replace("%player%", p.getName()));
                    return;
            }
        }
    }

}
