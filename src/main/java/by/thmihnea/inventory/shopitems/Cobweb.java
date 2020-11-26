package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.runnables.CobwebTask;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Cobweb implements Listener {

    public static void init(Arena arena, Player player) {
        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.cobweb")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.COBWEB.parseMaterial(), "§6Throwable Web", "", "§7§oThrow some cobweb on the ground to", "§7§ohelp you out escape from the attackers.", "", "§6• §fPrice: §e$" + TowerDefense.cfg.getInt("prices.cobweb")));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.cobweb"));
    }

    @EventHandler
    public void onWebUse(PlayerInteractEvent e) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(e.getPlayer());
        if (arena == null) return;
        if (arena.isAttacker(e.getPlayer())) return;

        Player p = e.getPlayer();
        if (p.getItemInHand().getType() != XMaterial.COBWEB.parseMaterial()) return;
        e.setCancelled(true);

        ItemStack throwable = p.getInventory().getItemInHand();
        ItemStack throwStack = new ItemStack(throwable);
        throwStack.setAmount(1);

        int amount = throwable.getAmount();
        Location location = p.getEyeLocation();

        Item thrownThrowable = p.getWorld().dropItem(location, throwStack);
        thrownThrowable.setVelocity(location.getDirection().multiply(1.1));
        thrownThrowable.setPickupDelay(500);
        throwable.setAmount(amount - 1);
        p.getInventory().setItemInHand(throwable);

        new CobwebTask(thrownThrowable);
    }

    @EventHandler
    public void onMerge(ItemMergeEvent e) {
        if (!(e.getEntity() instanceof Item)) return;
        Item item = (Item) e.getEntity();
        if (item.getItemStack().getType() != XMaterial.COBWEB.parseMaterial()) return;
        e.setCancelled(true);
    }

}
