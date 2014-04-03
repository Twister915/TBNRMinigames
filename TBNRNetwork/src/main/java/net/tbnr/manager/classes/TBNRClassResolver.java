package net.tbnr.manager.classes;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClassResolver;
import net.tbnr.manager.TBNRPlayer;

import java.util.Collection;

public class TBNRClassResolver extends GearzClassResolver<TBNRPlayer,TBNRAbstractClass> {
    @Override
    public Class<? extends TBNRAbstractClass> getClassForPlayer(TBNRPlayer player, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {
        return null;
    }

    @Override
    public void playerUsedClassFully(TBNRPlayer player, TBNRAbstractClass classUsed, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {

    }

    @Override
    public void gameStarting(Collection<TBNRPlayer> players, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {

    }
}
