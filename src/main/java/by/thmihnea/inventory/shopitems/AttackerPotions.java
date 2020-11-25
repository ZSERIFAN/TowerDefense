package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.item.PotionUtil;
import org.bukkit.entity.Player;

public class AttackerPotions {

    public static void init(Arena arena, Player player, String type) {
        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices." + type + "_pot")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        switch (type) {
            case "healing":
                player.getInventory().addItem(PotionUtil.createHealing());
                break;
            case "strength":
                player.getInventory().addItem(PotionUtil.createStrength());
                break;
            case "regen":
                player.getInventory().addItem(PotionUtil.createRegen());
                break;
            case "speed":
                player.getInventory().addItem(PotionUtil.createSwiftness());
                break;
        }

        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices." + type + "_pot"));
    }
}
