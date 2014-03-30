package net.tbnr.minigame.eg;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.manager.TBNRPlugin;
import org.bukkit.Bukkit;

/**
 * Created by Joey on 12/31/13.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final class GEnderzGame extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(EnderzGameArena.class, EnderzGameGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "genderzgame";
    }
}
