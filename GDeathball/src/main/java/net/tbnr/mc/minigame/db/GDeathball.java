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

package net.tbnr.mc.minigame.db;

import net.cogzmc.engine.gearz.GearzException;
import net.tbnr.mc.manager.TBNRPlugin;
import org.bukkit.Bukkit;

/**
 * Created by rigor789 on 2013.12.19..
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final class GDeathball extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(DBArena.class, DBGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "deathball";
    }
}
