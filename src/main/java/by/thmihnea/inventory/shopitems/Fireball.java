package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
        if (p.getItemInHand().getType() == XMaterial.FIRE_CHARGE.parseMaterial()) {
            if (p.getItemInHand().getAmount() == 1) {
                p.getInventory().removeItem(p.getItemInHand());
            } else {
                p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
            }
            Location location = p.getLocation();
            Entity f = location.getWorld().spawnEntity(location.add(location.add(0, 1, 0).getDirection()), EntityType.FIREBALL);
            f.setVelocity(location.getDirection().multiply(1.5));
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntity().getType() == EntityType.FIREBALL) {
            e.setCancelled(true);
            e.getLocation().getWorld().createExplosion(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ(), 2.5f, false, false);
        }
    }
}
