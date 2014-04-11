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

package net.tbnr.manager.event;

import net.tbnr.manager.TBNRPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerXPChangeEvent extends Event {
    private final Integer oldXp;
    private final Integer newXp;
    private final TBNRPlayer player;
    private static final HandlerList handlers = new HandlerList();

    public PlayerXPChangeEvent(Integer oldXp, Integer newXp, TBNRPlayer player) {
        this.oldXp = oldXp;
        this.newXp = newXp;
        this.player = player;
    }

    /**
     * Get the player this event relates to.
     *
     * @return A GearzPlayer object
     */
    public TBNRPlayer getPlayer() {
        return player;
    }

    @SuppressWarnings("unused")
    public Integer getNewXp() {
        return newXp;
    }

    @SuppressWarnings("unused")
    public Integer getOldXp() {
        return oldXp;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
