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

import com.google.common.collect.Lists;
import net.gearz.settings.SettingBuilder;
import net.gearz.settings.base.BaseSetting;
import net.gearz.settings.type.BooleanType;
import net.tbnr.gearz.server.ServerManager;
import net.tbnr.gearz.settings.PlayerSettings;
import net.tbnr.util.player.TPlayerDisconnectEvent;
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
        ServerManager.addPlayer(event.getPlayer().getPlayerName());
        event.getPlayer().removeAllPotionEffects();
        if (player.hasPermission("gearz.flight")) {
            player.setAllowFlight(true);
        }
        if (PlayerSettings.getManager(player).getValue(PlayerThings.TEXTURE_PACK, Boolean.class)) {
            player.setResourcePack(rescPackLink);
        }
    }

    public static BaseSetting TEXTURE_PACK = new SettingBuilder()
            .name("TexturePack")
            .type(new BooleanType())
            .defaultValue(true)
            .description("Toggle the TBNR texture pack.")
            .aliases(Lists.newArrayList("ResourcePack"))
            .get();

    @EventHandler
    public void onQuit(TPlayerDisconnectEvent event) {
        ServerManager.removePlayer(event.getPlayer().getPlayerName());
    }
}
