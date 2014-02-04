package net.tbnr.commerce.items;

import lombok.Getter;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.shop.Shop;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.player.GearzPlayer;
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

    @Getter private final HashMap<GearzPlayer, Shop> players;

    {
        players = new HashMap<>();
    }

    @TCommand(
            senders = {TCommandSender.Player},
            usage = "/shop <>",
            permission = "gearz.commerce.shop",
            name = "shop"
    )
    public TCommandStatus manageCactus(CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        GearzPlayer gearzPlayer = GearzPlayer.playerFromPlayer((Player) sender);
        if (players.containsKey(gearzPlayer)) return TCommandStatus.INVALID_ARGS;
        synchronized (players) {
            Shop shop = new Shop(gearzPlayer, GearzCommerce.getInstance().getItemAPI());
            players.put(gearzPlayer, shop);
            shop.open();
        }
        return TCommandStatus.SUCCESSFUL;
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.getInstance().handleCommandStatus(status, sender, senderType);
    }
}
