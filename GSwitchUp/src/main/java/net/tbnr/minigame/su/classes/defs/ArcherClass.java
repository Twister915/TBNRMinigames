package net.tbnr.minigame.su.classes.defs;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.classes.GearzClassMeta;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.minigame.su.classes.SwitchUpClass;

@GearzClassMeta(
        name = "Archer",
        key = "su_archer"
)
public final class ArcherClass extends SwitchUpClass {
    public ArcherClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }

    @Override
    protected String getFilename() {
        return "archer.json";
    }
}
