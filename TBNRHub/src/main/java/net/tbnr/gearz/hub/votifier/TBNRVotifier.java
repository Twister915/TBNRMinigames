package net.tbnr.gearz.hub.votifier;

import net.tbnr.gearz.hub.TBNRHub;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.gearz.votifier.VotifierListener;
import net.tbnr.util.player.TPlayer;
import net.tbnr.util.player.TPlayerDisconnectEvent;
import net.tbnr.util.player.TPlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Date;
import java.util.HashMap;

public class TBNRVotifier implements VotifierListener, Listener {
    private HashMap<GearzPlayer, Date> timeJoined;

    @Override
    public void onVote(GearzPlayer player, String site, Date time) {
        /*
        Log vote
         */
        TPlayer tPlayer = player.getTPlayer();
        Integer times_voted = (Integer) tPlayer.getStorable(TBNRHub.getInstance(), TimesVoted.getStaticName());
        if (times_voted == null) times_voted = 0;
        tPlayer.store(TBNRHub.getInstance(), new TimesVoted(times_voted +1)); //Store another vote
    }

    /*
    Flight implementation
     */

    @EventHandler
    public void onPlayerJoin(TPlayerJoinEvent event) {

    }
    @EventHandler
    public void onPlayerLeave(TPlayerDisconnectEvent event) {

    }

    void scheduleFlightDisable(GearzPlayer player) {

    }

    void storeFlightTime(GearzPlayer player, Integer minutes) {

    }

    void addFlightTime(GearzPlayer player, Integer minutes) {
        storeFlightTime(player, getFlightTime(player)+minutes);
    }

    Integer getFlightTime(GearzPlayer player) {
        return 0;
    }
}
