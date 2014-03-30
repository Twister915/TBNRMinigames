package net.tbnr.manager;

import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.util.player.TPlayer;

public final class TBNRPlayerProvider extends GearzPlayerProvider<TBNRPlayer> {
    @Override
    protected TBNRPlayer newInstanceFor(TPlayer player) {
        return new TBNRPlayer(player);
    }
}
