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
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * A simple module to send a link for
 * TBNR's leaderboards to players
 *
 * <p>
 * Latest Change: Create module
 * <p>
 *
 * @author Rigor
 * @since 1/25/2014
 */
public class StatsCommand implements TCommandHandler {
    @TCommand(name = "stats",
            senders = {TCommandSender.Player},
            permission = "gearz.stats",
            usage = "/stats")
    @SuppressWarnings("unused")
    public TCommandStatus stats(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        sender.sendMessage(TBNRNetworkManager.getInstance().getFormat("stats", true, new String[]{"<url>", TBNRNetworkManager.getInstance().getFormat("stats-url", true, new String[]{"<player>", sender.getName()})}));
        return TCommandStatus.SUCCESSFUL;
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.handleCommandStatus(status, sender);
    }
}
