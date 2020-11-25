package by.thmihnea.util.item;

import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SelectorUtil {

    public static void giveSelector(Player p) {
        p.getInventory().setItem(4, ItemCreateUtil.createItem(Material.NETHER_STAR, "§eArena Selector §7§o(Right-click)", "§7§oRight-click this to select an arena!"));
    }

}
