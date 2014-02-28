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

package net.tbnr.minigame.eg;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;
import org.bukkit.Bukkit;

/**
 * Created by Joey on 12/31/13.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final class GEnderzGame extends GearzPlugin {
    @Override
    public void enable() {
        try {
            registerGame(EnderzGameArena.class, EnderzGameGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "genderzgame";
    }
}
