package net.tbnr.minigame.sg.classes;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClassMeta;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.HideClassFromGUI;
import net.tbnr.manager.classes.TBNRAbstractClass;

@HideClassFromGUI
@GearzClassMeta(
        name = "Normal Class",
        key = "sg_normal"
)
public final class NormalClass extends TBNRAbstractClass{
    public NormalClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }
}
