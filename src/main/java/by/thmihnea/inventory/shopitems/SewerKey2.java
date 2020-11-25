package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.multiversion.XSound;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SewerKey2 implements Listener {

    public static void init(Arena arena, Player player) {
        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.sewer_key_2")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.TRIPWIRE_HOOK.parseMaterial(), "§6Sewer Key #2", "§7§oPoint in a direction and open", "§7§othe door in front of you."));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.sewer_key_2"));
    }

    @EventHandler
    public void onKeyUse(PlayerInteractEvent e) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(e.getPlayer());
        if (arena == null) return;
        if (arena.isDefender(e.getPlayer())) return;
        Player p = e.getPlayer();
        if (p.getItemInHand().getType() != XMaterial.TRIPWIRE_HOOK.parseMaterial()) return;
        if (!p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6Sewer Key #2")) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        e.setCancelled(false);
        if (e.getClickedBlock().getType() == XMaterial.QUARTZ_BLOCK.parseMaterial()) {
            p.getInventory().remove(p.getItemInHand());
            p.playSound(p.getLocation(), XSound.BLOCK_DISPENSER_DISPENSE.parseSound(), 1.0f, 1.0f);
            openDoor(p, e.getClickedBlock());
        }
    }

    public void openDoor(Player player, Block block) {
        block.setType(Material.AIR);
        player.playEffect(block.getLocation(), Effect.SMOKE, 5);
        for (int x = block.getX() - 1; x <= block.getX() + 1; x++) {
            for (int y = block.getY() - 1; y <= block.getY() + 1; y++) {
                for (int z = block.getZ() - 1; z <= block.getZ() + 1; z++) {
                    Location location = new Location(block.getWorld(), x, y, z);
                    Block bl = location.getBlock();
                    if (bl.getType() == XMaterial.QUARTZ_BLOCK.parseMaterial()) {
                        openDoor(player, bl);
                    }
                }
            }
        }
    }

}
