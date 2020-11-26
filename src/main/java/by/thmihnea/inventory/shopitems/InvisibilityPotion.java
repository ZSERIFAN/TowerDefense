package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.item.PotionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.UUID;

public class InvisibilityPotion implements Listener {

    public static void init(Arena arena, Player player) {

        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.invisibility")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        player.getInventory().addItem(PotionUtil.createPotion(PotionType.INVISIBILITY, 1, false, false, "§6Invisibility Potion", "§7§oUse this potion to gain a small invisibility", "§7§obuff for 5 seconds."));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.invisibility"));
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != XMaterial.POTION.parseMaterial()) return;
        e.setCancelled(false);
        ItemStack i = e.getItem();
        Player p = e.getPlayer();
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        if (arena == null) return;
        if (arena.isDefender(p)) {
            e.setCancelled(true);
            return;
        }
        if (i.getItemMeta().getDisplayName().equalsIgnoreCase("§6Invisibility Potion")) {
            p.getItemInHand().setAmount(0);
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, TowerDefense.cfg.getInt("invis_duration") * 20, 1));
            for (UUID uuid : arena.getDefenders()) {
                Player def = Bukkit.getPlayer(uuid);
                def.hidePlayer(p);
                Bukkit.getScheduler().scheduleSyncDelayedTask(TowerDefense.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        def.showPlayer(p);
                    }
                }, TowerDefense.cfg.getInt("invis_duration") * 20);
            }
        }
    }

}
