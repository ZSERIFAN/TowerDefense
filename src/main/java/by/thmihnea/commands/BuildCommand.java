package by.thmihnea.commands;

import by.thmihnea.TowerDefense;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.player.TDPlayer;
import by.thmihnea.util.item.SelectorUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuildCommand implements CommandExecutor {

    private TowerDefense plugin;

    public BuildCommand(TowerDefense plugin) {
        this.plugin = plugin;

        plugin.getCommand("build").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (!(p.hasPermission("td.build"))) {
            p.sendMessage(Lang.USER_IS_NOT_ADMIN.toString());
            return true;
        }
        TDPlayer tdPlayer = TDPlayer.tdPlayers.get(p.getUniqueId());
        if (tdPlayer.canBypassBuild()) {
            tdPlayer.setCanBypassBuild(false);
            p.sendMessage(Lang.BUILD_MODE_OFF.toString());
            p.setGameMode(GameMode.SURVIVAL);
            SelectorUtil.giveSelector(p);
        } else {
            tdPlayer.setCanBypassBuild(true);
            p.sendMessage(Lang.BUILD_MODE_ON.toString());
            p.setGameMode(GameMode.CREATIVE);
            p.getInventory().clear();
        }
        return false;
    }
}
