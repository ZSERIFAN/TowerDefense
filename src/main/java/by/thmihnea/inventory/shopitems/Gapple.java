package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.entity.Player;

public class Gapple {

    public static void init(Arena arena, Player player) {

        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.gapple")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.GOLDEN_APPLE.parseMaterial(), "§6Golden Apple", "§7§oRegenerate your health back while", "§7§oalso not using the ability to fight."));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.gapple"));

    }
}
