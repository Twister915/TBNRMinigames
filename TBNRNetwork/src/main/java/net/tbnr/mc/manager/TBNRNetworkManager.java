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

package net.tbnr.mc.manager;

import lombok.Getter;
import net.cogzmc.engine.gearz.network.GearzNetworkManagerPlugin;
import net.tbnr.mc.manager.classes.ClassPassUtils;
import net.tbnr.mc.manager.command.ClearChat;
import net.tbnr.mc.manager.command.StatsCommand;
import net.tbnr.mc.manager.command.YoutuberCommand;
import org.bukkit.Bukkit;

@SuppressWarnings("FieldCanBeLocal")
public final class TBNRNetworkManager extends GearzNetworkManagerPlugin<TBNRPlayer, TBNRPlayerProvider> {
    @Getter private static TBNRNetworkManager instance;

    @Override
    public void enable() {
        TBNRNetworkManager.instance = this;
        super.enable();
        TBNRPlayerUtils tbnrPlayerUtils = new TBNRPlayerUtils();
        registerCommands(tbnrPlayerUtils);
        registerCommands(new ClearChat());
        registerCommands(new YoutuberCommand());
        registerCommands(new ClassPassUtils());
        registerCommands(new StatsCommand());
        registerEvents(tbnrPlayerUtils);
    }

    @Override
    public String getStorablePrefix() {
        return "tbnrnetwork";
    }

    @Override
    protected TBNRPlayerProvider getNewPlayerProvider() {
        return new TBNRPlayerProvider();
    }

    @Override
    public String getServerPrefix() {
        return getFormat("formats.prefix", true);
    }

    @Override
    protected void onPlayerJoin(final TBNRPlayer player) {
        player.getTPlayer().setScoreboardSideTitle(TBNRNetworkManager.getInstance().getFormat("formats.sidebar-title-loading"));
        player.setupScoreboard();
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                player.updateStats();
            }
        }, 5);
    }
}
