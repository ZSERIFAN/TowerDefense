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

public class SwordUpgrade {

    public static void init(Arena arena, Player player) {

        ItemStack sword = ItemCreateUtil.createItem(XMaterial.IRON_SWORD.parseMaterial(), "§6Royal Sword", "§7§oThis is the upgraded version of the", "§7§omMighty Sword that you received upon", "§7§othe beggining of this game. Use it to slay", "§7§oall of your opponents!");

        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.sword_upgrade")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        for (ItemStack i : player.getInventory().getContents()) {
            if (i == null || i.getType() == Material.AIR) continue;
            if (i.getType().toString().contains("SWORD")) {
                sword.setType(i.getType());
                player.getInventory().removeItem(i);
                break;
            }
        }

        if (sword.getType() == XMaterial.DIAMOND_SWORD.parseMaterial()) {
            player.sendMessage("§cYou can't upgrade your sword anymore! (Already a Diamond Sword)");
            player.getInventory().addItem(sword);
            return;
        } else if (sword.getType() == XMaterial.IRON_SWORD.parseMaterial()) {
            sword.setType(XMaterial.DIAMOND_SWORD.parseMaterial());
            player.getInventory().addItem(sword);
        } else if (sword.getType() == XMaterial.STONE_SWORD.parseMaterial()) {
            sword.setType(XMaterial.IRON_SWORD.parseMaterial());
            player.getInventory().addItem(sword);
        } else if (sword.getType() == XMaterial.WOODEN_SWORD.parseMaterial()) {
            sword.setType(XMaterial.STONE_SWORD.parseMaterial());
            player.getInventory().addItem(sword);
        }

        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.sword_upgrade"));
        player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1.0f, 1.0f);
    }
}
