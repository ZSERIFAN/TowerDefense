package by.thmihnea.util.item;

import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShopItemUtil {

    public static void giveShop(Player p) {
        p.getInventory().setItem(8, ItemCreateUtil.createItem(Material.getMaterial("WATCH"), "§6Shop §7§o(Right-click)", "§7§oRight-click this to buy better gear!"));
    }

}
