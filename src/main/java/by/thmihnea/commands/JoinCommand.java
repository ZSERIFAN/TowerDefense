package by.thmihnea.commands;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.util.item.BarrierLeaveUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JoinCommand implements CommandExecutor {

    private TowerDefense plugin;

    public JoinCommand(TowerDefense plugin) {
        this.plugin = plugin;

        plugin.getCommand("join").setExecutor(this);
    }

    public boolean isInGame(Player p) {
        for (Arena arena : plugin.arenaHandler.getArenas())
            if (arena.inArena(p)) return true;
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        TDPlayer tdp = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (isInGame(p)) {
            p.sendMessage(Lang.ALREADY_IN_GAME.toString());
            return true;
        }
        if (tdp.isEditing()) {
            p.sendMessage(Lang.CANT_JOIN_IN_EDIT_MODE.toString());
            return true;
        }
        if (args.length < 1) {
            p.sendMessage(Lang.SPECIFY_ARENA_TO_JOIN.toString());
            return true;
        }
        else if (args.length >= 1) {
            if (plugin.getArenaHandler().getArenaByName(args[0]) == null) {
                p.sendMessage(Lang.ARENA_NOT_FOUND.toString());
                return true;
            }
            Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByName(args[0]);
            if (!arena.getState().equals(GameState.WAITING) && !arena.getState().equals(GameState.COUNTDOWN)) {
                p.sendMessage(Lang.CANT_JOIN_ARENA.toString());
                return true;
            }
            if (arena.getPlayers().size() >= 5) {
                p.sendMessage(Lang.ARENA_IS_FULL.toString());
                return true;
            }
            arena.addPlayer(p);
            if (!arena.getPlayers().isEmpty()) {
                for (UUID uuid : arena.getPlayers()) {
                    Player i = Bukkit.getPlayer(uuid);
                    i.sendMessage(Lang.PLAYER_JOINED_YOUR_ARENA.toString().replace("%player%", p.getName()).replace("%inArena%", String.valueOf(arena.getPlayers().size())));
                }
            }
            p.sendMessage(Lang.JOINED_ARENA.toString().replace("%arena%", arena.getName()));
            p.teleport(arena.getSpawnPoints().get("lobby"));
            p.getInventory().clear();
            BarrierLeaveUtil.giveLeaveBarrier(p);
        }
        return false;
    }
}
