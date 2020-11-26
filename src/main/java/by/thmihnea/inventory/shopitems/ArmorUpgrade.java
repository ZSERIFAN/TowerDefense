package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XSound;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.util.item.ArmorUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ArmorUpgrade {

    public static void init(Arena arena, Player player) {

        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.armor_upgrade")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        int i = 0; // 1 - leather; 2 - gold; 3 - iron; 4 - diamond;
        String str = player.getInventory().getChestplate().getType().toString();
        if (str.contains("LEATHER")) i = 1;
        else if (str.contains("GOLD")) i = 2;
        else if (str.contains("IRON")) i = 3;

        switch (i) {
            case 1:
                ArmorUtil.clearArmor(player);
                ArmorUtil.setArmor(player, "gold");
                break;
            case 2:
                ArmorUtil.clearArmor(player);
                ArmorUtil.setArmor(player, "iron");
                break;
            case 3:
            default:
                player.sendMessage("§cYou can't upgrade your armor anymore!");
                return;
        }

        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.armor_upgrade"));
        player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1.0f, 1.0f);

    }
}
