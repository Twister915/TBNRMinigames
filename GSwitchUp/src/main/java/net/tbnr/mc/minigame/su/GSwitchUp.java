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

package net.tbnr.mc.minigame.su;

import net.cogzmc.engine.gearz.GearzException;
import net.tbnr.mc.manager.TBNRPlugin;
import net.tbnr.mc.manager.classes.TBNRAbstractClass;
import net.tbnr.mc.manager.classes.TBNRClassSystem;
import net.tbnr.mc.minigame.su.classes.SwitchUpClassResolver;
import net.tbnr.mc.minigame.su.classes.defs.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

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
            List<Class<? extends TBNRAbstractClass>> classes = new ArrayList<>();
            classes.add(ArcherClass.class);
            classes.add(BoomerClass.class);
            classes.add(BowmanClass.class);
            classes.add(CheeseKnightClass.class);
            classes.add(CreativeBuilderClass.class);
            classes.add(GentlemanClass.class);
            classes.add(JuggernautClass.class);
            classes.add(MageClass.class);
            registerGame(GSwitchUpArena.class, GSwitchUpGame.class, new TBNRClassSystem(new SwitchUpClassResolver(), null, classes));
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
