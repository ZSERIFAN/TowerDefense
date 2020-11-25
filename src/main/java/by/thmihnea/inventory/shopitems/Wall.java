package by.thmihnea.inventory.shopitems;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.multiversion.XMaterial;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.runnables.Cooldown;
import by.thmihnea.runnables.CooldownType;
import by.thmihnea.util.ItemCreateUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Wall implements Listener {

    public static void init(Arena arena, Player player) {
        if (arena.getMoney(player) < TowerDefense.cfg.getInt("prices.wall")) {
            player.sendMessage(Lang.NOT_ENOUGH_MONEY.toString());
            return;
        }

        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(player.getUniqueId());

        if (tdPlayer.hasCooldown(CooldownType.WALL)) {
            player.sendMessage(Lang.CANT_USE_COOLDOWN.toString());
            return;
        }

        player.getInventory().addItem(ItemCreateUtil.createItem(XMaterial.STICK.parseMaterial(), "§6Create Wall", "§7§oPoint in a direction and create", "§7§oa wall in front of you."));
        arena.setMoney(player, arena.getMoney(player) - TowerDefense.cfg.getInt("prices.wall"));
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(e.getPlayer());
        if (arena == null) return;
        if (arena.isAttacker(e.getPlayer())) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = e.getPlayer();
        if (player.getItemInHand().getType() != XMaterial.STICK.parseMaterial()) return;
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(player.getUniqueId());
        if (tdPlayer.hasCooldown(CooldownType.WALL)) {
            player.sendMessage(Lang.CANT_USE_COOLDOWN.toString().replace("%timeLeft%", String.valueOf(tdPlayer.getCooldownByType(CooldownType.WALL).timeLeftInSeconds())));
            return;
        }
        if (player.getItemInHand().getAmount() == 1) {
            player.getInventory().removeItem(player.getItemInHand());
        } else {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        }

        Location origin = player.getEyeLocation();
        Vector direction = origin.getDirection().multiply(4);

        Location center = origin.add(direction);
        Vector rotation = getRotationDirection(origin);

        Vector initialRotation = rotation.clone().multiply(5 / 2);
        Location blockLocation = center.clone().add(initialRotation).subtract(0, 5 / 2, 0);
        rotation.multiply(-1);

        int initialX = blockLocation.getBlockX();
        int initialZ = blockLocation.getBlockZ();

        for (int y = 0; y < 7; y++) {
            for (int i = 0; i < 7; i++) {
                Block block = blockLocation.getBlock();
                block.setType(XMaterial.BLACK_WOOL.parseMaterial());
                blockLocation.add(rotation);
            }
            blockLocation.add(0, 1, 0);
            blockLocation.setX(initialX);
            blockLocation.setZ(initialZ);
        }

        Cooldown wall = new Cooldown(arena, player, CooldownType.WALL, TowerDefense.cfg.getInt("cooldowns.wall"));
        tdPlayer.addCooldown(wall);
    }

    private Vector getRotationDirection(Location origin) {
        Location rotation = origin.clone();
        rotation.setPitch(0);
        rotation.setYaw(rotation.getYaw() - 90);

        return rotation.getDirection();
    }
}
