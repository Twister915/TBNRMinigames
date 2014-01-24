package net.tbnr.minigame.su;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;
import org.bukkit.Bukkit;

/**
 * Created by Joey on 1/3/14.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final class GSwitchUp extends GearzPlugin {
    @Override
    public void enable() {
        try {
            registerGame(GSwitchUpArena.class, GSwitchUpGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "switchup";
    }
}
