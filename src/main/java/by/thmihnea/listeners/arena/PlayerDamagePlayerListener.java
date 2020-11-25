package by.thmihnea.listeners.arena;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.runnables.Cooldown;
import by.thmihnea.runnables.CooldownType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerDamagePlayerListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        Entity damaged = e.getEntity();
        Entity damager = e.getDamager();

        if (!(damaged instanceof Player) || !(damager instanceof Player)) return;
        Player p1 = (Player) damaged;
        Player p2 = (Player) damager;

        TDPlayer tdp1 = TDPlayer.tdPlayers.get(p1.getUniqueId());
        TDPlayer tdp2 = TDPlayer.tdPlayers.get(p2.getUniqueId());

        if (!(tdp1.isInGame()) || !(tdp2.isInGame())) return;

        Arena arena1 = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p1);
        Arena arena2 = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p2);
        if (!(arena1.equals(arena2))) return;

        if (arena1.isAttacker(p1) && arena1.isAttacker(p2)) e.setCancelled(true);
        else if (arena1.isDefender(p1) && arena1.isDefender(p2)) e.setCancelled(true);

        p1.showPlayer(p2);
        p2.showPlayer(p1);
        if (tdp1.hasCooldown(CooldownType.COMBAT)) tdp1.getCooldownByType(CooldownType.COMBAT).resetTime();
        else {
            Cooldown cp1 = new Cooldown(arena1, p1, CooldownType.COMBAT, 5);
            tdp1.addCooldown(cp1);
        }
        if (tdp2.hasCooldown(CooldownType.COMBAT)) tdp2.getCooldownByType(CooldownType.COMBAT).resetTime();
        else {
            Cooldown cp2 = new Cooldown(arena2, p2, CooldownType.COMBAT, 5);
            tdp2.addCooldown(cp2);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Entity damager = e.getEntity();
        Entity damaged = e.getDamager();

        if (!(damaged instanceof Player)) return;
        if (!(damager instanceof Zombie) && !(damager instanceof Skeleton) && !(damager instanceof IronGolem) && !(damager instanceof Witch) && !(damager instanceof Wolf)) return;

        Player p = (Player) damaged;

        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        if (arena == null) return;

        if (arena.isDefender(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        e.getDrops().clear();
    }
}
