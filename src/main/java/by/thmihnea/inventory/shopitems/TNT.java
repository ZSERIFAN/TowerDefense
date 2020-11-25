package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;

public class TNT implements Listener {

    public static void init(Arena arena, Player player) {
        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.wall_breaker")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.TNT.parseMaterial(), "§6Wall Breaker", "§7§oAllows you to break walls placed", "§7§oby the defenders."));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.wall_breaker"));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(player);
        if (arena == null) return;
        if (arena.isDefender(player)) {
            e.setCancelled(true);
            return;
        }
        if (e.getBlock().getType() == XMaterial.TNT.parseMaterial()) {
            if (e.isCancelled()) e.setCancelled(false);
            if (player.getItemInHand().getAmount() == 1) {
                player.getInventory().removeItem(player.getItemInHand());
            } else {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            }
            e.setCancelled(true);
            Location location = e.getBlock().getLocation();
            location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntityType() != EntityType.PRIMED_TNT) return;
        for (Block b : new ArrayList<Block>(e.blockList())) {
            if (b.getType() != XMaterial.BLACK_WOOL.parseMaterial())
                e.blockList().remove(b);
        }
        e.setYield(0);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof TNTPrimed) {
            e.setCancelled(true);
            return;
        }
    }

}
