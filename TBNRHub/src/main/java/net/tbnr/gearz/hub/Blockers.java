package net.tbnr.gearz.hub;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Blockers implements Listener {

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getPlayer().hasPermission("gearz.hub.fall")) return;
        if (player.getLocation().getY() < 0) {
            player.teleport(TBNRHub.getInstance().getSpawn().getSpawn());
            player.playSound(TBNRHub.getInstance().getSpawn().getSpawn(), Sound.CHICKEN_EGG_POP, 20, 1);
            player.sendMessage(TBNRHub.getInstance().getFormat("tpd-spawn", true, new String[]{"<prefix>", TBNRHub.getInstance().getChatPrefix()}));
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getPlayer().hasPermission("gearz.hub.drop")) return;
        if (event.getItemDrop().getItemStack().getType() == Material.SNOW_BALL) return;
        event.getPlayer().sendMessage(TBNRHub.getInstance().getFormat("cant-drop", true, new String[]{"<prefix>", TBNRHub.getInstance().getChatPrefix()}));
        event.setCancelled(true);
    }
}