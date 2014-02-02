package net.tbnr.commerce.purchase;

import net.tbnr.gearz.Gearz;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 1/29/14
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseCommandInterface implements TCommandHandler {
    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.getInstance().handleCommandStatus(status, sender, senderType);
    }
}
