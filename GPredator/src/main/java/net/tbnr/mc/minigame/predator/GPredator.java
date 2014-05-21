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

package net.tbnr.mc.minigame.predator;

import net.cogzmc.engine.gearz.GearzException;
import net.tbnr.mc.manager.TBNRPlugin;

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
