package by.thmihnea.listeners.arena;

import by.thmihnea.events.ArenaBreakOrbEvent;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.runnables.OrbReplace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaBreakOrbListener implements Listener {

    @EventHandler
    public void onOrbBreak(ArenaBreakOrbEvent e) {
        if (e.getOrbNumber() == 3) {
            e.getArena().broadCastMessage(Lang.ARENA_WIN_ATTACKERS.toString());
            e.getArena().stopGameTimer();
        } else {
            new OrbReplace(e.getArena(), e.getBlock(), e.getOrbNumber(), 10);
        }
    }
}
