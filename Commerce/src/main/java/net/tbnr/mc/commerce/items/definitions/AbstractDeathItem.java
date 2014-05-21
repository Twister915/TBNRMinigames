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

package net.tbnr.mc.commerce.items.definitions;

import net.cogzmc.engine.gearz.GearzException;
import net.cogzmc.engine.gearz.event.player.PlayerGameDeathEvent;
import net.tbnr.mc.commerce.items.CommerceItem;
import net.tbnr.mc.commerce.items.CommerceItemAPI;
import net.tbnr.mc.manager.TBNRPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public abstract class AbstractDeathItem extends CommerceItem {
    public AbstractDeathItem(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGameDeath(PlayerGameDeathEvent event) {
        if (!event.getDead().equals(getPlayer())) return;
        performDeathAction();
    }

    protected abstract void performDeathAction();
}
