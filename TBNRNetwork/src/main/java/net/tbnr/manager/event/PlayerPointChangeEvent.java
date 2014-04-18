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

import lombok.Getter;
import lombok.Setter;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerPointChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final TBNRPlayer player;
    @Getter
    private final Integer oldPoints;
    @Getter
    private final Integer newPoints;
    @Getter @Setter
    private Integer points;
    private boolean canceled;

    public PlayerPointChangeEvent(TBNRPlayer player, Integer oldPoints, Integer newPoints, Integer points) {
        this.player = player;
        this.oldPoints = oldPoints;
        this.newPoints = newPoints;
        this.points = points;
        this.canceled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }
}
