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

package net.tbnr.manager;

import lombok.Getter;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.network.GearzNetworkManagerPlugin;
import net.tbnr.manager.classes.ClassPassUtils;
import net.tbnr.manager.command.ClearChat;
import net.tbnr.manager.command.YoutuberCommand;
import org.bukkit.Bukkit;

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
    protected void onPlayerJoin(final TBNRPlayer player) {
        player.getTPlayer().setScoreboardSideTitle(Gearz.getInstance().getFormat("formats.sidebar-title-loading"));
        player.setupScoreboard();
        Bukkit.getScheduler().runTaskLater(Gearz.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.updateStats();
            }
        }, 5);
    }
}
