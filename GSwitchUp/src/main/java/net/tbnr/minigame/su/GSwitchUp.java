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

package net.tbnr.minigame.su;

import net.tbnr.gearz.GearzException;
import net.tbnr.manager.TBNRPlugin;
import net.tbnr.manager.classes.TBNRClassSystem;
import net.tbnr.minigame.su.classes.SwitchUpClassResolver;
import net.tbnr.minigame.su.classes.defs.*;
import org.bukkit.Bukkit;

/**
 * Created by Joey on 1/3/14.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final class GSwitchUp extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(GSwitchUpArena.class, GSwitchUpGame.class, new TBNRClassSystem(new SwitchUpClassResolver(),
                    ArcherClass.class, BoomerClass.class, BowmanClass.class, CheeseKnightClass.class, CreativeBuilderClass.class, GentlemanClass.class,
                    JuggernautClass.class, MageClass.class, OlympianClass.class, VikingClass.class));
        } catch (GearzException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "switchup";
    }
}
