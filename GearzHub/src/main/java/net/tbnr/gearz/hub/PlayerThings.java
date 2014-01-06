package net.tbnr.gearz.hub;

import net.tbnr.util.player.TPlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 8/31/13
 * Time: 7:27 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unused")
public class PlayerThings implements Listener {

    @EventHandler
    public void onJoin(TPlayerJoinEvent event) {
        if (event.getPlayer().getPlayer().hasPermission("gearz.flight")) {
            event.getPlayer().getPlayer().setAllowFlight(true);
        }
    }
}
