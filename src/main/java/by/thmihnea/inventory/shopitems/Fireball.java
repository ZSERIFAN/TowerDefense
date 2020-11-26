package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.listeners.hub.PlayerDamageListener;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.runnables.Respawn;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class Fireball implements Listener {

    public static void init(Arena arena, Player player) {

        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.fire_shot")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.FIRE_CHARGE.parseMaterial(), "§6Fireball", "§7§oShoot an explosive charge to", "§7§osurprise attack your enemies!"));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.fire_shot"));
    }

    @EventHandler
    public void shootFireball(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (p.getItemInHand().getType() == XMaterial.FIRE_CHARGE.parseMaterial()) {
            if (p.getItemInHand().getAmount() == 1) {
                p.getInventory().removeItem(p.getItemInHand());
            } else {
                p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
            }
            Location location = p.getEyeLocation();
            Entity f = location.getWorld().spawnEntity(location.add(location.add(0, 0.25, 0).getDirection()), EntityType.FIREBALL);
            f.setCustomName(p.getUniqueId().toString());
            f.setCustomNameVisible(false);
            f.setVelocity(location.getDirection().multiply(1.5));
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntity().getType() == EntityType.FIREBALL) {
            e.setCancelled(true);
            e.setYield(0);
        }
    }

    @EventHandler
    public void onFireballDamage(EntityDamageByEntityEvent e) {
        Entity damaged = e.getEntity();
        Entity damager = e.getDamager();

        if (!(damaged instanceof Player)) return;
        Player p1 = (Player) damaged;
        if (!(damager instanceof LargeFireball) && !(damager instanceof org.bukkit.entity.Fireball)) return;

        Player p2 = Bukkit.getPlayer(UUID.fromString(damager.getCustomName()));

        Arena arena1 = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p1);
        Arena arena2 = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p2);
        if ((arena1 != arena2) || arena1 == null || arena2 == null) return;
        if (arena1.isSameTeam(p1, p2)) {
            e.setCancelled(true);
            e.setDamage(0.0D);
        }

    }
}
