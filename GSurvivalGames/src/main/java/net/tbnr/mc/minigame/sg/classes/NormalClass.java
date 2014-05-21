package net.tbnr.mc.minigame.sg.classes;

import net.cogzmc.engine.gearz.game.GearzGame;
import net.cogzmc.engine.gearz.game.classes.GearzClassMeta;
import net.tbnr.mc.manager.TBNRPlayer;
import net.tbnr.mc.manager.classes.HideClassFromGUI;
import net.tbnr.mc.manager.classes.TBNRAbstractClass;

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
