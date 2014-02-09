/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerPointChangeEvent;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.player.TPlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.util.Date;

public abstract class AbstractPointBoost extends CommerceItem {
    private final String message;
    private final double percentage;
    public AbstractPointBoost(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
        this.percentage = ((double)percentageBoost()/100)+1;
        this.message = GearzCommerce.getInstance().getFormat("point-boost-message", false, new String[]{"<perc>", String.valueOf(percentageBoost())});
    }
    @EventHandler
    public void onPointGain(PlayerPointChangeEvent event) {
        if (!event.getPlayer().equals(this.getPlayer())) return;
        TPlayer tPlayer = event.getPlayer().getTPlayer();
        tPlayer.sendMessage(this.message);
        tPlayer.playSound(Sound.ORB_PICKUP);
        event.setPoints(Double.valueOf(Math.ceil(event.getPoints() * percentage)).intValue());
        if (shouldRevoke()) revoke();
    }

    private boolean shouldRevoke() {
        Date date_time = getObject("date_time", Date.class);
        long l = date_time.getTime() + (daysLength() * 86400);
        return new Date().getTime()  > l;
    }
    public abstract int percentageBoost();
    public abstract int daysLength();
}
