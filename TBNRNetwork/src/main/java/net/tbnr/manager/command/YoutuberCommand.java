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

package net.tbnr.manager.command;

import net.tbnr.gearz.Gearz;
import net.tbnr.manager.TBNRNetworkManager;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.api.YoutubeChannel;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * <p>
 * Latest Change:
 * <p>
 *
 * @author Jake
 * @since 4/13/2014
 */
public class YoutuberCommand implements TCommandHandler {
    @TCommand(
            name = "youtuber",
            usage = "/youtuber <player>",
            permission = "gearz.youtuber.lookup",
            senders = {TCommandSender.Player, TCommandSender.Console})
    @SuppressWarnings("unused")
    public TCommandStatus youtuber(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length != 1) return TCommandStatus.INVALID_ARGS;
        String youtuberSearch = args[0];
        Player player = Bukkit.getPlayerExact(youtuberSearch);
        if (player == null) return TCommandStatus.INVALID_ARGS;
        TBNRPlayer tbnrYoutuber = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(player);
        String youtubeChannel = tbnrYoutuber.getYoutubeChannel();
        if (youtubeChannel == null) {
            sender.sendMessage(Gearz.getInstance().getFormat("formats.no-youtube", true));
            return TCommandStatus.SUCCESSFUL;
        }
        sendYoutubeData(sender, youtuberSearch);
        return TCommandStatus.SUCCESSFUL;
    }

    @TCommand(
            name = "setyoutube",
            usage = "/setyoutube <username>",
            permission = "gearz.youtuber.set",
            senders = {TCommandSender.Player})
    @SuppressWarnings("unused")
    public TCommandStatus setYoutube(final CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length != 1) return TCommandStatus.INVALID_ARGS;
        final String toSet = args[0];
        new YoutubeChannel(toSet, new YoutubeChannel.YoutuberCallback() {
            @Override
            public void onComplete(YoutubeChannel youtubeChannel) {
                sender.sendMessage(Gearz.getInstance().getFormat("formats.set-youtube-this", true));
                sender.sendMessage(formatData("Username:", youtubeChannel.getUsername()));
                sender.sendMessage(formatData("Subscribers:", youtubeChannel.getSubscriberCount() + ""));
                sender.sendMessage(formatData("Views:", youtubeChannel.getViewCount() + ""));
                sender.sendMessage(formatData("Location:", youtubeChannel.getLocation()));
                sender.sendMessage(formatData("URL:", youtubeChannel.getChannelURL()));
                TBNRPlayer tbnrYoutuber = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer((Player) sender);
                tbnrYoutuber.setYoutubeChannel(toSet);
            }

            @Override
            public void onFail(YoutubeChannel youtubeChannel, String errorMessage) {
                sender.sendMessage(Gearz.getInstance().getFormat("formats.fail-set", true));
            }
        });
        return TCommandStatus.SUCCESSFUL;
    }

    private void sendYoutubeData(final CommandSender receiever, String youtuber) {
        new YoutubeChannel(youtuber, new YoutubeChannel.YoutuberCallback() {
            @Override
            public void onComplete(YoutubeChannel youtubeChannel) {
                receiever.sendMessage(formatData("Username:", youtubeChannel.getUsername()));
                receiever.sendMessage(formatData("Subscribers:", youtubeChannel.getSubscriberCount() + ""));
                receiever.sendMessage(formatData("Views:", youtubeChannel.getViewCount() + ""));
                receiever.sendMessage(formatData("Location:", youtubeChannel.getLocation()));
                receiever.sendMessage(formatData("URL:", youtubeChannel.getChannelURL()));
            }

            @Override
            public void onFail(YoutubeChannel youtubeChannel, String errorMessage) {
                receiever.sendMessage(Gearz.getInstance().getFormat("formats.fail-youtube", true));
            }
        });
    }

    private String formatData(String key, String value) {
        return "  " + Gearz.getInstance().getFormat("formats.youtube-display", false, new String[]{"<key>", key}, new String[]{"<value>", value});
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.getInstance().handleCommandStatus(status, sender, senderType);
    }
}
