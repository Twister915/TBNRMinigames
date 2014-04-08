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

package net.tbnr.gearz.hub;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Colors signs.
 */
public class ColoredSigns implements Listener {
    /**
     * This is the event handler for a sign change event.
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void onSignChange(SignChangeEvent event) {
        if (!event.getPlayer().hasPermission("gearz.coloredsigns")) return;
        for (int x = 0, l = event.getLines().length; x < l; x++) {
            event.setLine(x, ChatColor.translateAlternateColorCodes('&', event.getLine(x))); //Color the f****** line
        }
    }
}
