package net.tbnr.minigame.eg;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;
import org.bukkit.Bukkit;

/**
 * Created by Joey on 12/31/13.
 */
public final class GEnderzGame extends GearzPlugin {
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
