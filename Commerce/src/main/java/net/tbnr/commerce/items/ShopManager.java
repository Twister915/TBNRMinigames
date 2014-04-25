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

package net.tbnr.commerce.items;

import lombok.Getter;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.shop.PlayerShop;
import net.tbnr.commerce.items.shop.Shop;
import net.tbnr.gearz.Gearz;
import net.tbnr.manager.TBNRNetworkManager;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 2/4/14
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShopManager implements TCommandHandler {

    @Getter private final HashMap<TBNRPlayer, PlayerShop> players;

    {
        players = new HashMap<>();
    }

    @TCommand(
            senders = {TCommandSender.Player},
            usage = "/shop <>",
            permission = "gearz.commerce.shop",
            name = "shop"
    )
    public TCommandStatus shopCommand(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (!Gearz.getInstance().isLobbyServer()) {
            sender.sendMessage(GearzCommerce.getInstance().getFormat("formats.must-be-hub"));
            return TCommandStatus.SUCCESSFUL;
        }
        TBNRPlayer player = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer((Player) sender);
        synchronized (players) {
            PlayerShop shop = new Shop(player, GearzCommerce.getInstance().getItemAPI());
            players.put(player, shop);
            shop.open();
        }
        return TCommandStatus.SUCCESSFUL;
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.handleCommandStatus(status, sender);
    }
}
