/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.gearz.hub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.tbnr.gearz.netcommand.BouncyUtils;
import net.tbnr.gearz.server.Server;
import net.tbnr.gearz.server.ServerManager;
import net.tbnr.util.inventory.ServerSelector;
import net.tbnr.util.inventory.base.BaseGUI;
import net.tbnr.util.inventory.base.GUICallback;
import net.tbnr.util.inventory.base.GUIItem;
import net.tbnr.util.player.TPlayer;
import net.tbnr.util.player.TPlayerManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake on 12/27/13.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public class BlastOffSigns implements Listener {
    final private Map<TPlayer, SignData> inUse = new HashMap<>();

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (!(block.getState() instanceof Sign)) return;
        Sign sign = (Sign) block.getState();
        final String[] lines = sign.getLines();
        if (ServerManager.getServersWithGame(lines[1]).size() == 0) return;
        if (!lines[0].equals(TBNRHub.getInstance().getFormat("formats.blastoff-topline", true))) return;
        final ServerSelector serverSelector = new ServerSelector(lines[1], new GUICallback() {
            @Override
            public void onItemSelect(BaseGUI gui, GUIItem item, Player player) {

                /**
                 * The reason you need to test as the person could have the selector
                 * open for a while, and he clicks the last item while a server is restarting
                 * so the server is no longer online and therefore is not in the servers list
                 * Though the inventory is already open so it's not updated
                 * Therefore it causes and IndexOutOfBoundsException
                 * @see java.lang.IndexOutOfBoundsException
                 */
                ServerSelector selector = (ServerSelector) gui;
                Server server;
                try {
                    server = selector.getServers().get(
                            /** if */item.getSlot() > selector.getServers().size() ?
                            /** true */0 : /** false */item.getSlot()
                    );
                } catch (IndexOutOfBoundsException e) {
                    return;
                }

                if (server != null && server.isCanJoin()) {
                    selector.close(player);
                    SignData signData = new SignData(server, player.getLocation().getBlockY());
                    inUse.put(TPlayerManager.getInstance().getPlayer(player), signData);
                    player.setVelocity(new Vector(0, 4, 0));
                }
            }

            @Override
            public void onGUIOpen(BaseGUI gui, Player player) {}

            @Override
            public void onGUIClose(BaseGUI gui, Player player) {}
        });
        serverSelector.open(event.getPlayer());
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        TPlayer tPlayer = TPlayerManager.getInstance().getPlayer(player);
        if (!inUse.containsKey(tPlayer)) return;
        Integer y = event.getTo().getBlockY();
        SignData signData = inUse.get(tPlayer);
        /**
         * This handles if a player hit a block before reaching the distance
         */
        if (event.getTo().getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
            BouncyUtils.sendPlayerToServer(player, signData.getServer().getBungee_name());
            inUse.remove(tPlayer);
            return;
        }
        if (y - signData.getStart() >= TBNRHub.getInstance().getConfig().getInt("blastoff.distance")) {
            BouncyUtils.sendPlayerToServer(player, signData.getServer().getBungee_name());
            inUse.remove(tPlayer);
        }

    }

    @AllArgsConstructor
    @Data
    @EqualsAndHashCode
    public class SignData {
        private Server server;
        private Integer start;
    }
}