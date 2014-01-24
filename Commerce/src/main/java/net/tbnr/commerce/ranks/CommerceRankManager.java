package net.tbnr.commerce.ranks;

import net.tbnr.gearz.Gearz;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

/**
 * Created by Joey on 1/18/14.
 */
public class CommerceRankManager implements Listener, TCommandHandler {

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.getInstance().handleCommandStatus(status, sender, senderType);
    }
}
