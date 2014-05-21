/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.mc.hub.votifier;

import net.cogzmc.engine.gearz.player.GearzPlayer;
import net.cogzmc.engine.util.player.TPlayer;
import net.cogzmc.engine.util.player.TPlayerDisconnectEvent;
import net.cogzmc.engine.util.player.TPlayerJoinEvent;
import net.tbnr.mc.hub.TBNRHub;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Date;
import java.util.HashMap;

public class TBNRVotifier implements /*VotifierListener,*/ Listener {
    private HashMap<GearzPlayer, Date> timeJoined;

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
