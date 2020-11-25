package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class WaterBucket implements Listener {

    public static void init(Arena arena, Player player) {
        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.water_bucket")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.WATER_BUCKET.parseMaterial(), "§6Water Bucket", "§7§oBuying this item will help you out", "§7§oescaping from hot situations."));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.water_bucket"));
    }

    @EventHandler
    public void onWaterUse(PlayerInteractEvent e) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(e.getPlayer());
        if (arena == null) return;
        if (arena.isDefender(e.getPlayer())) return;

        Player p = e.getPlayer();
        if (p.getItemInHand().getType() != XMaterial.WATER_BUCKET.parseMaterial()) return;
        if (p.getFireTicks() <= 0) {
            p.sendMessage("§cYou can't use this item right now as you're currently not on fire!");
            return;
        }
        e.setCancelled(true);
        p.setFireTicks(0);
        p.getInventory().remove(p.getItemInHand());
        p.sendMessage(Lang.SET_OFF_FIRE.toString());
    }
}
