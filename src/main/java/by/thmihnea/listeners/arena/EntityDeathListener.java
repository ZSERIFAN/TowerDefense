package by.thmihnea.listeners.arena;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.persistent.lang.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Player p = e.getEntity().getKiller();
        if (p == null) return;
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        if (arena == null) return;
        if (arena.isDefender(p)) return;
        switch (e.getEntity().getType()) {
            case IRON_GOLEM:
                arena.setMoney(p, arena.getMoney(p) + TowerDefense.cfg.getInt("income.golem_kill"));
                p.sendMessage(Lang.MONEY_BY_KILLING_ENTITY.toString().replace("%money%", String.valueOf(TowerDefense.cfg.getInt("income.golem_kill"))).replace("%entity%", "Golem"));
                break;
            case ZOMBIE:
                arena.setMoney(p, arena.getMoney(p) + TowerDefense.cfg.getInt("income.zombie_kill"));
                p.sendMessage(Lang.MONEY_BY_KILLING_ENTITY.toString().replace("%money%", String.valueOf(TowerDefense.cfg.getInt("income.zombie_kill"))).replace("%entity%", "Zombie"));
                break;
            case SKELETON:
                arena.setMoney(p, arena.getMoney(p) + TowerDefense.cfg.getInt("income.skeleton_kill"));
                p.sendMessage(Lang.MONEY_BY_KILLING_ENTITY.toString().replace("%money%", String.valueOf(TowerDefense.cfg.getInt("income.skeleton_kill"))).replace("%entity%", "Skeleton"));
                break;
            case WOLF:
                arena.setMoney(p, arena.getMoney(p) + TowerDefense.cfg.getInt("income.wolf_kill"));
                p.sendMessage(Lang.MONEY_BY_KILLING_ENTITY.toString().replace("%money%", String.valueOf(TowerDefense.cfg.getInt("income.wolf_kill"))).replace("%entity%", "Wolf"));
                break;
        }
    }
}
