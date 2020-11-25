package by.thmihnea.commands;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.persistent.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TCCommand implements CommandExecutor {

    private TowerDefense plugin;

    public TCCommand(TowerDefense plugin) {
        this.plugin = plugin;

        plugin.getCommand("tc").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        if (arena == null) {
            p.sendMessage(Lang.CHAT_NOT_IN_ARENA.toString());
            return false;
        }
        if (!arena.isAttacker(p) && !arena.isDefender(p)) {
            p.sendMessage(Lang.CHAT_NOT_IN_ARENA.toString());
            return false;
        }
        if (args.length == 0) {
            p.sendMessage(Lang.CHAT_SPECIFY_ARGS.toString());
            return false;
        }
        String msg = "";
        for (int i = 0; i < args.length; i++)
            msg = msg + args[i] + " ";
        if (arena.isAttacker(p)) {
            for (UUID uuid : arena.getAttackers()) {
                Player at = Bukkit.getPlayer(uuid);
                at.sendMessage(Lang.CHAT_ATTACKER.toString().replace("%player%", p.getName()).replace("%message%", msg));
            }
        } else {
            for (UUID uuid : arena.getDefenders()) {
                Player at = Bukkit.getPlayer(uuid);
                at.sendMessage(Lang.CHAT_DEFENDER.toString().replace("%player%", p.getName()).replace("%message%", msg));
            }
        }
        return true;
    }
}
