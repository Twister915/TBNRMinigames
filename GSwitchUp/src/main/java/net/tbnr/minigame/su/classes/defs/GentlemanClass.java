package net.tbnr.minigame.su.classes.defs;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClassMeta;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.minigame.su.classes.SwitchUpClass;

@GearzClassMeta(name = "GentlemanClass")
public final class GentlemanClass extends SwitchUpClass {
    public GentlemanClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }

    @Override
    protected String getFilename() {
        return "gentleman.json";
    }
}
