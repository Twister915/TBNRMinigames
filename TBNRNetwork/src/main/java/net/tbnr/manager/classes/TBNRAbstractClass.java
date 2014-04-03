package net.tbnr.manager.classes;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzAbstractClass;
import net.tbnr.manager.TBNRPlayer;

public abstract class TBNRAbstractClass extends GearzAbstractClass<TBNRPlayer> {
    public TBNRAbstractClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }
}
