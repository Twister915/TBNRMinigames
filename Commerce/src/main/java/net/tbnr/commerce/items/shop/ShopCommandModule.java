package net.tbnr.commerce.items.shop;

import net.tbnr.gearz.Gearz;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 2/4/14
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShopCommandModule implements TCommandHandler {

    @TCommand(
            senders = {TCommandSender.Player},
            usage = "/shop <>",
            permission = "gearz.commerce.shop",
            name = "shop"
    )
    public TCommandStatus manageCactus(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {

        //TODO Shop command


        return TCommandStatus.SUCCESSFUL;
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.getInstance().handleCommandStatus(status, sender, senderType);
    }
}
