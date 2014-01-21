package net.tbnr.gearz.hub;

import net.tbnr.util.player.TPlayerJoinEvent;
import org.bukkit.entity.Player;
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

    private final String rescPackLink;

    public PlayerThings() {
        rescPackLink = TBNRHub.getInstance().getConfig().getString("resource-pack-link");
    }

    @EventHandler
    public void onJoin(TPlayerJoinEvent event) {
        Player player = event.getPlayer().getPlayer();
        if (player.hasPermission("gearz.flight")) {
            player.setAllowFlight(true);
        }
        player.setResourcePack(rescPackLink);
    }
}
