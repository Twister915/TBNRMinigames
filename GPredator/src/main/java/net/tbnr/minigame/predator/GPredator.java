package net.tbnr.minigame.predator;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;

/**
 * Created by George on 11/01/14.
 * <p/>
 * Purpose Of File: Main Plugin File for GPredator
 * <p/>
 * Latest Change: Added it
 */
public class GPredator extends GearzPlugin {
    @Override
    public void enable() {
        try {
            registerGame(PredatorArena.class, PredatorGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "predator";
    }
}
