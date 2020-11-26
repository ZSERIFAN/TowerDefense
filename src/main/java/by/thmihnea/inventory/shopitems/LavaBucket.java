package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.runnables.Cooldown;
import by.thmihnea.runnables.CooldownType;
import by.thmihnea.runnables.LavaRemovalTask;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class LavaBucket implements Listener {

    public static void init(Arena arena, Player player) {
        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.lava_trap")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(player.getUniqueId());

        if (tdPlayer.hasCooldown(CooldownType.LAVA_TRAP)) {
            player.sendMessage(Lang.CANT_USE_COOLDOWN.toString().replace("%timeLeft%", String.valueOf(tdPlayer.getCooldownByType(CooldownType.LAVA_TRAP).timeLeftInSeconds())));
            return;
        }

        player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.BLAZE_ROD.parseMaterial(), "§6Lava Trap", "§7§oBuying this item will help you out", "§7§oby creating a stronger offensive plan for your strategy."));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.lava_trap"));
    }

    @EventHandler
    public void onLavaUse(PlayerInteractEvent e) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(e.getPlayer());
        if (arena == null) return;
        if (arena.isAttacker(e.getPlayer())) return;
        Player p = e.getPlayer();
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (p.getItemInHand().getType() != XMaterial.BLAZE_ROD.parseMaterial()) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (tdPlayer.hasCooldown(CooldownType.LAVA_TRAP)) {
            p.sendMessage(Lang.CANT_USE_COOLDOWN.toString().replace("%timeLeft%", String.valueOf(tdPlayer.getCooldownByType(CooldownType.LAVA_TRAP).timeLeftInSeconds())));
            return;
        }
        e.setCancelled(true);
        if (p.getInventory().getItemInHand().getAmount() > 1)
            p.getInventory().getItemInHand().setAmount(p.getInventory().getItemInHand().getAmount() - 1);
        else
            p.getInventory().setItemInHand(null);
        for (int x = e.getClickedBlock().getX() - 1; x <= e.getClickedBlock().getX() + 1; x++) {
            for (int z = e.getClickedBlock().getZ() - 1; z <= e.getClickedBlock().getZ() + 1; z++) {
                Location location = new Location(p.getWorld(), x, e.getClickedBlock().getY(), z);
                arena.addTrap(location.getBlock());
            }
        }
        Cooldown lava = new Cooldown(arena, p, CooldownType.LAVA_TRAP, TowerDefense.cfg.getInt("cooldowns.lava_trap"));
        tdPlayer.addCooldown(lava);
        p.sendMessage(Lang.SETUP_TRAP.toString());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(e.getPlayer());
        if (arena == null) return;
        if (arena.isDefender(e.getPlayer())) return;
        if (arena.getTraps().isEmpty()) return;

        Player p = e.getPlayer();
        Location bLoc = new Location(p.getWorld(), e.getTo().getX(), e.getTo().getY() - 1, e.getTo().getZ());
        Block block = bLoc.getBlock();
        if (arena.getTraps().contains(block)) {
            p.sendMessage(Lang.FALL_IN_TRAP.toString());
            setupLava(arena, block);
            new LavaRemovalTask(block, TowerDefense.cfg.getInt("timers.lava_trap_removal"));
        }
    }

    public void setupLava(Arena arena, Block block) {
        block.setType(XMaterial.LAVA.parseMaterial());
        arena.removeTrap(block);
        for (int x = block.getX() - 1; x <= block.getX() + 1; x++) {
            for (int z = block.getZ() - 1; z <= block.getZ() + 1; z++) {
                Location location = new Location(block.getWorld(), x, block.getY(), z);
                Block bl = location.getBlock();
                if (arena.getTraps().contains(bl))
                    setupLava(arena, bl);
            }
        }
    }

    @EventHandler
    public void onLiquidFlow(BlockFromToEvent e) {
        if (e.getBlock().getType() == XMaterial.LAVA.parseMaterial())
            e.setCancelled(true);
    }

}
