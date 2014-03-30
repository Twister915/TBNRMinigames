package net.tbnr.manager;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.game.GearzPlayerProvider;

public abstract class TBNRPlugin extends GearzPlugin<TBNRPlayer> {
    @Override
    protected GearzPlayerProvider<TBNRPlayer> getPlayerProvider() {
        return TBNRNetworkManager.getInstance().getPlayerProvider();
    }
}
