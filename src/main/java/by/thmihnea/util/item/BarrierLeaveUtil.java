package by.thmihnea.util.item;

import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BarrierLeaveUtil {

    public static void giveLeaveBarrier(Player p) {
        p.getInventory().setItem(8, ItemCreateUtil.createItem(Material.BARRIER, "§cQuit Arena §7§o(Right-click)", "§7§oRight-click this to leave the current arena."));
    }

}
