package net.tbnr.minigame.db;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.manager.TBNRPlugin;
import org.bukkit.Bukkit;

/**
 * Created by rigor789 on 2013.12.19..
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final class GDeathball extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(DBArena.class, DBGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "deathball";
    }
}
