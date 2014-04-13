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

package net.tbnr.gearz.hh;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.hh.classes.HeadHunterClassResolver;
import net.tbnr.gearz.hh.classes.def.Artillery;
import net.tbnr.gearz.hh.classes.def.Normal;
import net.tbnr.manager.TBNRPlugin;
import net.tbnr.manager.classes.TBNRClassSystem;

/**
 * HeadHunter class
 */
public final class GHeadHunter extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(HeadHunterArena.class, HeadHunterGame.class, new TBNRClassSystem(new HeadHunterClassResolver(), Artillery.class, Normal.class));
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
