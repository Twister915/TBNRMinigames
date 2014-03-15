package net.tbnr.gearz.hub;

import net.tbnr.gearz.server.ServerManager;
import net.tbnr.util.player.TPlayerDisconnectEvent;
import net.tbnr.util.player.TPlayerJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

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
        ServerManager.addPlayer(event.getPlayer().getPlayerName());
        if (player.hasPermission("gearz.flight")) {
            player.setAllowFlight(true);
        }
        player.setResourcePack(rescPackLink);
    }

    @EventHandler
    public void onQuit(TPlayerDisconnectEvent event) {
        ServerManager.removePlayer(event.getPlayer().getPlayerName());
    }

    @EventHandler
    public void inventoryMove(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) event.setCancelled(true);
    }
}
