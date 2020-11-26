package by.thmihnea.listeners.hub;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.player.TDPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerBlockBreakListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        e.getBlock().getDrops().clear();
        if (tdPlayer.canBypassBuild()) return;
        if (tdPlayer.isInGame()) {
            if (e.getBlock().getType() == XMaterial.GLASS.parseMaterial()) return;
            if (e.getBlock().getType().toString().toUpperCase().contains("WOOL") && arena.isDefender(p)) return;
            if (e.getBlock().getType() == XMaterial.COBWEB.parseMaterial() && arena.isAttacker(p)) return;
            e.setCancelled(true);
        } else {
            if (!tdPlayer.canBypassBuild()) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (e.getBlock().getType() == XMaterial.TNT.parseMaterial()) return;
        if (tdPlayer.canBypassBuild()) return;
        if (tdPlayer.isInGame()) {
            e.setCancelled(true);
        } else {
            if (!tdPlayer.canBypassBuild()) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
