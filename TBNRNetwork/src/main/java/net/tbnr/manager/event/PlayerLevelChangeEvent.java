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

package net.tbnr.manager.event;


import net.tbnr.manager.TBNRPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerLevelChangeEvent extends Event {
    private final Integer oldLevel;
    private final Integer newLevel;
    private final TBNRPlayer player;
    private static final HandlerList handlers = new HandlerList();

    public PlayerLevelChangeEvent(Integer oldLevel, Integer newLevel, TBNRPlayer player) {
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.player = player;
    }

    public TBNRPlayer getPlayer() {
        return player;
    }

    public Integer getNewLevel() {
        return newLevel;
    }

    public Integer getOldLevel() {
        return oldLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
