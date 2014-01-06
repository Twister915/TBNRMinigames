package net.tbnr.gearz.hub;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Colors signs.
 */
public class ColoredSigns implements Listener {
    /**
     * This is the event handler for a sign change event.
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void onSignChange(SignChangeEvent event) {
        if (!event.getPlayer().hasPermission("gearz.coloredsigns")) return;
        for (int x = 0; x < event.getLines().length; x++) {
            event.setLine(x, ChatColor.translateAlternateColorCodes('&', event.getLine(x))); //Color the fucking line
        }
    }
}
