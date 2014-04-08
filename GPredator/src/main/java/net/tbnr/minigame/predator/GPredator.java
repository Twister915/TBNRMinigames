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

package net.tbnr.minigame.predator;

import net.tbnr.gearz.GearzException;
import net.tbnr.manager.TBNRPlugin;

/**
 * Created by George on 11/01/14.
 * <p/>
 * Purpose Of File: Main Plugin File for GPredator
 * <p/>
 * Latest Change: Added it
 */
public class GPredator extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(PredatorArena.class, PredatorGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "predator";
    }
}
