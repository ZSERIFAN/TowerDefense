package by.thmihnea.arena;

import by.thmihnea.TowerDefense;
import by.thmihnea.events.ArenaBreakOrbEvent;
import by.thmihnea.multiversion.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Orb implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        if (arena == null) return;
        if (e.getBlock().getType() != XMaterial.GREEN_STAINED_GLASS.parseMaterial()) return;
        if (arena.isDefender(p)) {
            e.setCancelled(true);
            return;
        }
        arena.broadCastMessage("§6" + p.getName() + " broke the orb!");
        arena.setOrbBreak(arena.getOrbBreak() + 1);
        e.getBlock().getWorld().createExplosion(e.getBlock().getLocation(), 2, true);
        e.setCancelled(true);
        e.getBlock().setType(XMaterial.EMERALD_BLOCK.parseMaterial());
        ArenaBreakOrbEvent event = new ArenaBreakOrbEvent(arena, p, arena.getOrbBreak(), e.getBlock());
        Bukkit.getPluginManager().callEvent(event);
    }
}
