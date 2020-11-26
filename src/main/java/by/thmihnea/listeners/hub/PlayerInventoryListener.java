package by.thmihnea.listeners.hub;

import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.player.TDPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) return;
        Player p = (Player) e.getWhoClicked();
        TDPlayer tdp = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (tdp.canBypassBuild()) return;
        if (tdp.isInGame()) return;
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClockMove(InventoryClickEvent e) {
        if (e.isCancelled()) return;
        Player p = (Player) e.getWhoClicked();
        TDPlayer tdp = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (tdp.canBypassBuild()) return;
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        if (!clickedItem.hasItemMeta()) return;
        if (clickedItem.getType() == by.thmihnea.multiversion.XMaterial.CLOCK.parseMaterial() ||
        clickedItem.getType().toString().toUpperCase().contains("HELMET") ||
        clickedItem.getType().toString().toUpperCase().contains("CHESTPLATE") ||
        clickedItem.getType().toString().toUpperCase().contains("LEGGINGS") ||
        clickedItem.getType().toString().toUpperCase().contains("BOOTS")) e.setCancelled(true);
    }

    @EventHandler
    public void onClockDrop(PlayerDropItemEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (tdPlayer.canBypassBuild()) return;
        if (e.getItemDrop().getItemStack().getType() == XMaterial.CLOCK.parseMaterial() ||
        e.getItemDrop().getItemStack().getType().toString().toUpperCase().contains("HELMET") ||
                e.getItemDrop().getItemStack().getType().toString().toUpperCase().contains("CHESTPLATE") ||
                e.getItemDrop().getItemStack().getType().toString().toUpperCase().contains("LEGGINGS") ||
                e.getItemDrop().getItemStack().getType().toString().toUpperCase().contains("BOOTS") ||
                e.getItemDrop().getItemStack().getType().toString().toUpperCase().contains("SWORD")) e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (tdPlayer.canBypassBuild()) return;
        if (tdPlayer.isInGame()) return;
        e.setCancelled(true);
    }
}
