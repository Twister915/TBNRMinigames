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

package net.tbnr.minigame.ascension;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.manager.TBNRPlugin;
import org.bukkit.Bukkit;


public final class GAscension extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(ASArena.class, ASGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public String getStorablePrefix() {
        return "ascension";
    }
}
