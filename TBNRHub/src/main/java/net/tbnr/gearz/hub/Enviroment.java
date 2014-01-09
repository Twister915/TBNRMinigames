package net.tbnr.gearz.hub;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by rigor789 on 2013.12.21..
 */
public class Enviroment implements Listener {

    @EventHandler
    @SuppressWarnings("unused")
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onCraftItemEvent(CraftItemEvent e) {
        if (!e.getWhoClicked().hasPermission("gearz.staff")) {
            e.setCancelled(true);
        }
    }
}
