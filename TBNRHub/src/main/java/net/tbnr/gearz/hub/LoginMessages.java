package net.tbnr.gearz.hub;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 9/12/13
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginMessages implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    @SuppressWarnings("unused")
    public void onJoin(PlayerJoinEvent event) {
        if (TBNRHub.getInstance().getConfig().getBoolean("hide-stream")) {
            event.setJoinMessage(null);
            return;
        }
        event.setJoinMessage(TBNRHub.getInstance().getFormat("join-message", false, new String[]{"<player>", event.getPlayer().getPlayer().getName()}));
        if (TBNRHub.getInstance().getConfig().getBoolean("welcome-messages")) {
            Bukkit.broadcastMessage(TBNRHub.getInstance().getFormat("welcome-message", false, new String[]{"<player>", event.getPlayer().getPlayer().getName()}));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    @SuppressWarnings("unused")
    public void onQuit(PlayerQuitEvent event) {
        if (TBNRHub.getInstance().getConfig().getBoolean("hide-stream")) {
            event.setQuitMessage(null);
            return;
        }
        event.setQuitMessage(TBNRHub.getInstance().getFormat("quit-message", false, new String[]{"<player>", event.getPlayer().getPlayer().getName()}));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    @SuppressWarnings("unused")
    public void onPlayerKick(PlayerKickEvent event) {
        if (TBNRHub.getInstance().getConfig().getBoolean("hide-stream")) {
            event.setLeaveMessage(null);
            return;
        }
        event.setLeaveMessage(TBNRHub.getInstance().getFormat("quit-message", false, new String[]{"<player>", event.getPlayer().getPlayer().getName()}));
    }
}
