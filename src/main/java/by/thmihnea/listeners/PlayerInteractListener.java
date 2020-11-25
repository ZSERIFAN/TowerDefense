package by.thmihnea.listeners;

import by.thmihnea.inventory.ArenaSelector;
import by.thmihnea.inventory.ArenaShop;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.runnables.CooldownType;
import by.thmihnea.util.item.ItemUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (p.getInventory().getItemInHand().getType() == Material.AIR || p.getInventory().getItemInHand().getType() == null) return;
        if (!p.getInventory().getItemInHand().getItemMeta().hasDisplayName()) return;
        switch (p.getInventory().getItemInHand().getItemMeta().getDisplayName()) {
            case "§eArena Selector §7§o(Right-click)":
                ArenaSelector.selector.open(p);
                break;
            case "§cQuit Arena §7§o(Right-click)":
                p.performCommand("leave");
                break;
            case "§6Shop §7§o(Right-click)":
                if (tdPlayer.hasCooldown(CooldownType.COMBAT)) {
                    p.sendMessage(Lang.CANT_OPEN_SHOP_IN_COMBAT.toString().replace("%timeLeft%", String.valueOf(tdPlayer.getCooldownByType(CooldownType.COMBAT).timeLeftInSeconds())));
                    break;
                }
                ArenaShop.shop.open(p);
                break;
        }
    }
    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.SPECTATOR)) e.setCancelled(true);
    }

}
