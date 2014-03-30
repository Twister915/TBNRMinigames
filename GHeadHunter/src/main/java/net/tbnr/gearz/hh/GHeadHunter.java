package net.tbnr.gearz.hh;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.manager.TBNRPlugin;

/**
 * HeadHunter class
 */
public final class GHeadHunter extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(HeadHunterArena.class, HeadHunterGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "headhunter";
    }
}
