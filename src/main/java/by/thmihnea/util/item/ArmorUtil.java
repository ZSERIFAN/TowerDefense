package by.thmihnea.util.item;

import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;

public class ArmorUtil {

    public static void setupDefenderItems(Player p) {
        p.getInventory().clear();
        p.getInventory().setHelmet(createItemDef(Material.LEATHER_HELMET, "§aDefender Helmet", "defender", "", "§7§oBy equipping this piece", "§7§oof armor you become one", "§7§oof the mighty Royal Castle defenders.", "", "§f§lBASIC ARMOR PIECE"));
        p.getInventory().setChestplate(createItemDef(Material.LEATHER_CHESTPLATE, "§aDefender Chestplate", "defender", "", "§7§oBy equipping this piece", "§7§oof armor you become one", "§7§oof the mighty Royal Castle defenders.", "", "§f§lBASIC ARMOR PIECE"));
        p.getInventory().setLeggings(createItemDef(Material.LEATHER_LEGGINGS, "§aDefender Leggings", "defender", "", "§7§oBy equipping this piece", "§7§oof armor you become one", "§7§oof the mighty Royal Castle defenders.", "", "§f§lBASIC ARMOR PIECE"));
        p.getInventory().setBoots(createItemDef(Material.LEATHER_BOOTS, "§aDefender Boots", "defender", "", "§7§oBy equipping this piece", "§7§oof armor you become one", "§7§oof the mighty Royal Castle defenders.", "", "§f§lBASIC ARMOR PIECE"));
        p.getInventory().addItem(ItemCreateUtil.createItem(Material.IRON_SWORD, "§aDefender Sword", "", "§7§oTier I - Sword", "§7§oUpgrade it using the shop."));
    }

    public static void setupAttackerItems(Player p) {
        p.getInventory().clear();
        p.getInventory().setHelmet(createItemAttacker(Material.LEATHER_HELMET, "§cAttacker Helmet", "attacker",  "§7§oBy equipping this piece", "§7§oof armor you become one", "§7§oof the raiders trying to kill the Guards.", "", "§f§lBASIC ARMOR PIECE"));
        p.getInventory().setChestplate(createItemAttacker(Material.LEATHER_CHESTPLATE, "§cAttacker Chestplate", "attacker", "§7§oBy equipping this piece", "§7§oof armor you become one", "§7§oof the raiders trying to kill the Guards.", "", "§f§lBASIC ARMOR PIECE"));
        p.getInventory().setLeggings(createItemAttacker(Material.LEATHER_LEGGINGS, "§cAttacker Leggings", "attacker",  "§7§oBy equipping this piece", "§7§oof armor you become one", "§7§oof the raiders trying to kill the Guards.", "", "§f§lBASIC ARMOR PIECE"));
        p.getInventory().setBoots(createItemAttacker(Material.LEATHER_BOOTS, "§cAttacker Boots", "attacker", "§7§oBy equipping this piece", "§7§oof armor you become one", "§7§oof the raiders trying to kill the Guards.", "", "§f§lBASIC ARMOR PIECE"));
        p.getInventory().addItem(ItemCreateUtil.createItem(Material.IRON_SWORD, "§cAttacker Sword", "", "§7§oTier I - Sword", "§7§oUpgrade it using the shop."));
    }

    public static ItemStack createItemDef(final Material material, final String name, String team, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setColor(Color.GREEN);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItemAttacker(final Material material, final String name, String team, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setColor(Color.RED);

        item.setItemMeta(meta);

        return item;
    }

    public static void clearArmor(Player player) {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public static void setArmor(Player player, String str) {
        switch (str) {
            case "gold":
                player.getInventory().setHelmet(ItemCreateUtil.createItem(XMaterial.GOLDEN_HELMET.parseMaterial(), "§6Royal Helmet", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                player.getInventory().setChestplate(ItemCreateUtil.createItem(XMaterial.GOLDEN_CHESTPLATE.parseMaterial(), "§6Royal Chestplate", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                player.getInventory().setLeggings(ItemCreateUtil.createItem(XMaterial.GOLDEN_LEGGINGS.parseMaterial(), "§6Royal Leggings", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                player.getInventory().setBoots(ItemCreateUtil.createItem(XMaterial.GOLDEN_BOOTS.parseMaterial(), "§6Royal Boots", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                break;
            case "iron":
                player.getInventory().setHelmet(ItemCreateUtil.createItem(XMaterial.IRON_HELMET.parseMaterial(), "§6Royal Helmet", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                player.getInventory().setChestplate(ItemCreateUtil.createItem(XMaterial.IRON_CHESTPLATE.parseMaterial(), "§6Royal Chestplate", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                player.getInventory().setLeggings(ItemCreateUtil.createItem(XMaterial.IRON_LEGGINGS.parseMaterial(), "§6Royal Leggings", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                player.getInventory().setBoots(ItemCreateUtil.createItem(XMaterial.IRON_BOOTS.parseMaterial(), "§6Royal Boots", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                break;
            case "diamond":
                player.getInventory().setHelmet(ItemCreateUtil.createItem(XMaterial.DIAMOND_HELMET.parseMaterial(), "§6Royal Helmet", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                player.getInventory().setChestplate(ItemCreateUtil.createItem(XMaterial.DIAMOND_CHESTPLATE.parseMaterial(), "§6Royal Chestplate", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                player.getInventory().setLeggings(ItemCreateUtil.createItem(XMaterial.DIAMOND_LEGGINGS.parseMaterial(), "§6Royal Leggings", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                player.getInventory().setBoots(ItemCreateUtil.createItem(XMaterial.DIAMOND_BOOTS.parseMaterial(), "§6Royal Boots", "§7§oUse this upgraded version of the Mighty Helmet", "§7§oto further strengthen your defense."));
                break;
        }
    }
}
