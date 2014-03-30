package net.tbnr.minigame.ascension;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.manager.TBNRPlugin;
import org.bukkit.Bukkit;


public final class GAscension extends TBNRPlugin {
    @Override
    public void enable() {
        try {
            registerGame(ASArena.class, ASGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public String getStorablePrefix() {
        return "ascension";
    }
}
