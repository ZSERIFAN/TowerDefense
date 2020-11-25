package by.thmihnea.listeners.arena;

import by.thmihnea.Util;
import by.thmihnea.arena.Arena;
import by.thmihnea.arena.GameState;
import by.thmihnea.events.PlayerLeaveArenaEvent;
import by.thmihnea.persistent.lang.Lang;
import by.thmihnea.runnables.PostgameTimer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaWinByLeaveEvent implements Listener {

    @EventHandler
    public void onArenaLeave(PlayerLeaveArenaEvent e) {
        Arena arena = e.getArena();
        Player p = e.getPlayer();

        if (!arena.getState().equals(GameState.IN_PROGRESS)) return;

        if (!arena.getAttackers().isEmpty() && !arena.getDefenders().isEmpty()) return;

        int i = -1;
        if (arena.getAttackers().isEmpty()) i = 0;
        else if (arena.getDefenders().isEmpty()) i = 1;

        switch (i) {
            case 0:
                arena.broadCastMessage(Lang.ARENA_WIN_DEFENDERS.toString());
                break;
            case 1:
                arena.broadCastMessage(Lang.ARENA_WIN_ATTACKERS.toString());
                break;
            case -1:
            default:
                Util.log(Util.LogType.WARNING, "ArenaWinByLeaveEvent has been somehow fired without the teams being emptied. Please report this error to thmDev.");
                return;
        }

        arena.broadCastMessage(Lang.ARENA_WIN_DUE_TO_LEAVE.toString().replace("%player%", p.getName()));
        arena.getGameTimer().clear();
        arena.setState(GameState.CLEANUP);
        new PostgameTimer(arena, 30);
    }
}
