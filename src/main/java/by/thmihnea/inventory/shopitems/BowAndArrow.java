package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.multiversion.XSound;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BowAndArrow {

    public static void init(Arena arena, Player player) {
        boolean foundBow = false;

        for (ItemStack i : player.getInventory().getContents()) {
            if (i == null || i.getType() == Material.AIR) continue;
            if (i.getType() == XMaterial.BOW.parseMaterial()) {
                foundBow = true;
                break;
            }
        }

        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.bowarrow")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.bowarrow"));

        if (!foundBow) {
            player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.BOW.parseMaterial(), "§6Revenant's Bow", "§7§oA powerful bow that allows you to kill", "§7§oyour enemies."));
            player.getInventory().addItem(new ItemStack(XMaterial.ARROW.parseMaterial(), 32));
        } else
            player.getInventory().addItem(new ItemStack(XMaterial.ARROW.parseMaterial(), 128));

        player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1.0f, 1.0f);
    }
}
