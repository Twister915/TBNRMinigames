package net.tbnr.minigame.su.classes.defs;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClassMeta;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.minigame.su.classes.SwitchUpClass;

@GearzClassMeta(name = "Mage")
public final class MageClass extends SwitchUpClass {
    public MageClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }

    @Override
    protected String getFilename() {
        return "mage.json";
    }
}
