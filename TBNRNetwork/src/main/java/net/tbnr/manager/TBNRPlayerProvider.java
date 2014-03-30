package net.tbnr.manager;

import net.tbnr.gearz.game.GearzPlayerProvider;
import net.tbnr.util.player.TPlayer;

public class TBNRPlayerProvider extends GearzPlayerProvider<TBNRPlayer> {
    @Override
    protected TBNRPlayer newInstanceFor(TPlayer player) {
        return new TBNRPlayer(player);
    }
}
