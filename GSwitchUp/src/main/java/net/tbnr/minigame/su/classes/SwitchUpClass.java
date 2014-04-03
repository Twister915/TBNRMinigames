package net.tbnr.minigame.su.classes;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.game.kits.GearzKit;
import net.tbnr.gearz.game.kits.GearzKitReadException;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRKitClass;

public abstract class SwitchUpClass extends TBNRKitClass {
    public SwitchUpClass(TBNRPlayer player, GearzGame game) {
        super(player, game);
    }

    @Override
    public void onPlayerActivate() {
        super.onPlayerActivate();
        getPlayer().getTPlayer().sendMessage(getGame().getPluginFormat("formats.give-class", true, new String[]{"<class>", getMeta().name()}));

    }

    @Override
    protected GearzKit constructKit() throws GearzKitReadException {
        return loadFromJSONResource(getFilename());
    }

    protected abstract String getFilename();
}
