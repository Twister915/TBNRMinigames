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
            player.teleport(GearzHub.getInstance().getSpawn().getSpawn());
            player.playSound(GearzHub.getInstance().getSpawn().getSpawn(), Sound.CHICKEN_EGG_POP, 20, 1);
            player.sendMessage(GearzHub.getInstance().getFormat("tpd-spawn", true, new String[]{"<prefix>", GearzHub.getInstance().getChatPrefix()}));
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getPlayer().hasPermission("gearz.hub.drop")) return;
        if (event.getItemDrop().getItemStack().getType() == Material.SNOW_BALL) return;
        event.getPlayer().sendMessage(GearzHub.getInstance().getFormat("cant-drop", true, new String[]{"<prefix>", GearzHub.getInstance().getChatPrefix()}));
        event.setCancelled(true);
    }
}
