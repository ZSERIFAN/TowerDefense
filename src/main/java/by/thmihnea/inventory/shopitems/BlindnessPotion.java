package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.item.PotionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class BlindnessPotion implements Listener {

    public static void init(Arena arena, Player player) {

        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.blindness")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        player.getInventory().addItem(PotionUtil.createBlindness());
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.blindness"));
    }
}
