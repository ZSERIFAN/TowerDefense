package by.thmihnea.listeners.arena;

import by.thmihnea.TowerDefense;
import by.thmihnea.arena.Arena;
import by.thmihnea.persistent.lang.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Iterator;

public class ArenaChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Iterator it = e.getRecipients().iterator();
        Player p = e.getPlayer();
        Arena arena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(p);
        if (arena == null) {
            e.setFormat(Lang.CHAT_FORMAT_LOBBY.toString().replace("%player%", p.getName()).replace("%message%", e.getMessage()));
            while (it.hasNext()) {
                Player recipient = (Player) it.next();
                Arena rArena = TowerDefense.getInstance().getArenaHandler().getArenaByPlayer(recipient);
                if (rArena != null) it.remove();
            }
        } else {
            while (it.hasNext()) {
                Player recipient = (Player) it.next();
                if (!p.getWorld().equals(recipient.getWorld())) it.remove();
            }
            e.setFormat(Lang.CHAT_FORMAT_ARENA.toString().replace("%player%", p.getName()).replace("%message%", e.getMessage()));
        }
    }
}
