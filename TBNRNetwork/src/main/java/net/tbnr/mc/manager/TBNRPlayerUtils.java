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

package net.tbnr.mc.manager;

import net.cogzmc.engine.gearz.Gearz;
import net.cogzmc.engine.util.command.TCommand;
import net.cogzmc.engine.util.command.TCommandHandler;
import net.cogzmc.engine.util.command.TCommandSender;
import net.cogzmc.engine.util.command.TCommandStatus;
import net.tbnr.mc.manager.event.PlayerLevelChangeEvent;
import net.tbnr.mc.manager.event.PlayerPointChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class TBNRPlayerUtils implements TCommandHandler, Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    @SuppressWarnings("unused")
    public void onLevelChange(PlayerLevelChangeEvent event) {
        event.getPlayer().getTPlayer().playSound(Sound.LEVEL_UP);
        event.getPlayer().getTPlayer().sendMessage(TBNRNetworkManager.getInstance().getFormat("formats.level-up", true, new String[]{"<level>", String.valueOf(event.getNewLevel())}, new String[]{"<old-level>", String.valueOf(event.getOldLevel())}));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPointChange(PlayerPointChangeEvent event) {
        event.getPlayer().getTPlayer().playSound(Sound.ORB_PICKUP);
    }

    @TCommand(
            name = "xp",
            description = "XP Management Command",
            senders = {TCommandSender.Console, TCommandSender.Player},
            permission = "gearz.xp",
            usage = "Set XP.")
    @SuppressWarnings("unused")
    public TCommandStatus xp(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length < 2 || (type == TCommandSender.Console && args.length < 3)) return TCommandStatus.FEW_ARGS;
        Player player = (type == TCommandSender.Console || args.length == 3) ? Bukkit.getPlayer(args[0]) : (Player) sender;
        if (player == null) return TCommandStatus.INVALID_ARGS;
        TBNRPlayer gplayer = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(player);
        int xp;
        try {
            xp = Integer.valueOf((type == TCommandSender.Console || args.length == 3) ? args[2] : args[1]);
        } catch (NumberFormatException ex) {
            if (args.length == 2) {
                return TCommandStatus.FEW_ARGS;
            } else {
                return TCommandStatus.INVALID_ARGS;
            }
        }
        switch (((type == TCommandSender.Console || args.length == 3) ? args[1] : args[0])) {
            case "add":
            case "+":
                gplayer.addXp(xp);
                break;
            case "subtract":
            case "remove":
            case "-":
                gplayer.addXp(-1 * xp);
                break;
            case "clear":
                gplayer.addXp(-1 * Integer.MAX_VALUE);
                xp = -1;
                break;
            default:
                return TCommandStatus.INVALID_ARGS;
        }
        sender.sendMessage(TBNRNetworkManager.getInstance().getFormat("formats.xp-change", true, new String[]{"<action>", (xp >= 0 ? "added" : "removed")}, new String[]{"<target>", (sender.equals(gplayer.getTPlayer().getPlayer()) ? "yourself" : gplayer.getTPlayer().getPlayer().getName())}, new String[]{"<xp>", String.valueOf(Math.abs(xp))}));
        if (!sender.equals(gplayer.getTPlayer().getPlayer())) gplayer.getTPlayer().sendMessage(TBNRNetworkManager.getInstance().getFormat("formats.xp-changed", true, new String[]{"<action>", (xp >= 0 ? "added" : "removed")}, new String[]{"<xp>", String.valueOf(xp)}));
        return TCommandStatus.SUCCESSFUL;
    }

    @TCommand(
            name = "points",
            description = "Points management command.",
            senders = {TCommandSender.Console, TCommandSender.Player},
            permission = "gearz.points",
            usage = "Set points.")
    @SuppressWarnings("unused")
    public TCommandStatus points(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length < 2 || (type == TCommandSender.Console && args.length < 3)) return TCommandStatus.FEW_ARGS;
        Player player = (type == TCommandSender.Console || args.length == 3) ? Bukkit.getPlayer(args[0]) : (Player) sender;
        if (player == null) return TCommandStatus.INVALID_ARGS;
        TBNRPlayer gplayer = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(player);
        int points;
        try {
            points = Integer.valueOf((type == TCommandSender.Console || args.length == 3) ? args[2] : args[1]);
        } catch (NumberFormatException ex) {
            if (args.length == 2) {
                return TCommandStatus.FEW_ARGS;
            } else {
                return TCommandStatus.INVALID_ARGS;
            }
        }
        switch (((type == TCommandSender.Console || args.length == 3) ? args[1] : args[0])) {
            case "add":
            case "+":
                gplayer.addPoints(points);
                break;
            case "subtract":
            case "remove":
            case "-":
                gplayer.addPoints(-1 * points);
                break;
            case "clear":
                gplayer.addPoints(-1 * Integer.MAX_VALUE);
                points = -1;
                break;
            default:
                return TCommandStatus.INVALID_ARGS;
        }
        sender.sendMessage(TBNRNetworkManager.getInstance().getFormat("formats.xp-change", true, new String[]{"<action>", (points >= 0 ? "added" : "removed")}, new String[]{"<target>", (sender.equals(gplayer.getTPlayer().getPlayer()) ? "yourself" : gplayer.getTPlayer().getPlayer().getName())}, new String[]{"<xp>", String.valueOf(Math.abs(points))}));
        if (!sender.equals(gplayer.getTPlayer().getPlayer())) gplayer.getTPlayer().sendMessage(TBNRNetworkManager.getInstance().getFormat("formats.xp-changed", true, new String[]{"<action>", (points >= 0 ? "added" : "removed")}, new String[]{"<xp>", String.valueOf(points)}));
        return TCommandStatus.SUCCESSFUL;
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.handleCommandStatus(status, sender);
    }
}
