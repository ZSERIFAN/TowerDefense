package by.thmihnea.listeners.arena;

import by.thmihnea.arena.Arena;
import by.thmihnea.events.MatchTimerTickEvent;
import by.thmihnea.runnables.GameTimer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaMatchTimerTick implements Listener {

    @EventHandler
    public void onMatchTimerTick(MatchTimerTickEvent e) {
        GameTimer gameTimer = e.getMatchTimer();
        Arena arena = gameTimer.getArena();
        int minutes = 0;
        long seconds = gameTimer.timeLeftInSeconds();
        while (seconds - 60 >= 0) {
            minutes++;
            seconds -= 60;
        }
        String aux = "";
        String minAux = "";
        if (seconds < 10) aux = "0";
        if (minutes < 10) minAux = "0";
        arena.broadCastActionBar("§fTime Left: §e" + minAux + minutes + "§8:§e" + aux + seconds + " §6•", true);
        arena.giveTeamMoney();
    }
}
