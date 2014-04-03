package net.tbnr.manager;

import lombok.Getter;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.network.GearzNetworkManagerPlugin;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.manager.classes.pass.ClassPassManager;
import net.tbnr.manager.command.UtilCommands;
import org.bukkit.Bukkit;

public final class TBNRNetworkManager extends GearzNetworkManagerPlugin<TBNRPlayer> {
    @Getter private TBNRPlayerProvider playerProvider;
    @Getter private static TBNRNetworkManager instance;
    @Getter private ClassPassManager classPassManager;

    @Override
    public void enable() {
        TBNRNetworkManager.instance = this;
        super.enable();
        TBNRPlayerUtils tbnrPlayerUtils = new TBNRPlayerUtils();
        registerCommands(tbnrPlayerUtils);
        registerCommands(new UtilCommands());
        registerEvents(tbnrPlayerUtils);
        classPassManager = new ClassPassManager();
    }

    @Override
    public String getStorablePrefix() {
        return "tbnrnetwork";
    }

    @Override
    protected GearzPlayerProvider<TBNRPlayer> getNewPlayerProvider() {
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
