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

package net.tbnr.mc.minigame.hh;

import net.cogzmc.engine.gearz.GearzException;
import net.tbnr.mc.manager.TBNRPlugin;

/**
 * HeadHunter class
 */
public final class GHeadHunter extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(HeadHunterArena.class, HeadHunterGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "headhunter";
    }
}
