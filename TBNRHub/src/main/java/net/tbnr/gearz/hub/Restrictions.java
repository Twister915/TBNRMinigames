/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.gearz.hub;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Restrictions implements Listener {

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getPlayer().hasPermission("gearz.hub.fall")) return;
        if (player.getLocation().getY() > 0) return;
        player.teleport(TBNRHub.getInstance().getSpawnHandler().getSpawn());
        player.playSound(TBNRHub.getInstance().getSpawnHandler().getSpawn(), Sound.CHICKEN_EGG_POP, 20, 1);
        player.sendMessage(TBNRHub.getInstance().getFormat("tpd-spawn", true, new String[]{"<prefix>", TBNRHub.getInstance().getChatPrefix()}));
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getPlayer().hasPermission("gearz.hub.drop")) return;
        if (event.getItemDrop().getItemStack().getType() == Material.SNOW_BALL) return;
        event.getPlayer().sendMessage(TBNRHub.getInstance().getFormat("cant-drop", true, new String[]{"<prefix>", TBNRHub.getInstance().getChatPrefix()}));
        event.setCancelled(true);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.toWeatherState()) e.setCancelled(true);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onCraftItemEvent(CraftItemEvent e) {
        if (e.getWhoClicked().hasPermission("gearz.staff")) return;
        e.setCancelled(true);
    }
}
