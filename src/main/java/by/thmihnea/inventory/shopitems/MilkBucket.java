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
import org.bukkit.potion.PotionEffect;

public class MilkBucket implements Listener {

    public static void init(Arena arena, Player player) {
        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.milk_bucket")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.MILK_BUCKET.parseMaterial(), "§6Milk Bucket", "§7§oBuying this item will help you out", "§7§oescaping from tricky situations."));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.milk_bucket"));
    }

    @EventHandler
    public void onMilkUse(PlayerInteractEvent e) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(e.getPlayer());
        if (arena == null) return;
        if (arena.isDefender(e.getPlayer())) return;

        Player p = e.getPlayer();
        if (p.getInventory().getItemInHand().getType() != XMaterial.MILK_BUCKET.parseMaterial()) return;

        if (p.getActivePotionEffects().isEmpty()) {
            p.sendMessage("§cYou can't use this item because you're currently not being poisoned!");
            return;
        }

        for (PotionEffect effect : p.getActivePotionEffects())
            p.removePotionEffect(effect.getType());
        p.sendMessage(Lang.SET_OFF_POISON.toString());
        p.getInventory().remove(p.getItemInHand());
    }

}
