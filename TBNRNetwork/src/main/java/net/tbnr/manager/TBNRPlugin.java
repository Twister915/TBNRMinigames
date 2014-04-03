package net.tbnr.manager;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.manager.classes.TBNRAbstractClass;

public abstract class TBNRPlugin extends GearzPlugin<TBNRPlayer, TBNRAbstractClass> {
    @Override
    protected GearzPlayerProvider<TBNRPlayer> getPlayerProvider() {
        return TBNRNetworkManager.getInstance().getPlayerProvider();
    }
}
