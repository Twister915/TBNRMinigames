package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerPriorityDetermineEvent;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

@CommerceItemMeta(
        tier = Tier.Standard,
        key = "fifty_premium_joins",
        humanName = "50x Premium Joins",
        item = Material.PAPER
)
public final class FiftyPremiumJoins extends CommerceItem {
    private Integer joinsLeft;
    public FiftyPremiumJoins(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
    @EventHandler
    public void onPlayerJoin(PlayerPriorityDetermineEvent event) {
        if (!event.getPlayer().equals(getPlayer())) return;
        event.setAbsolutePriority(true);
        event.getPlayer().getTPlayer().sendMessage(GearzCommerce.getInstance().getFormat("formats.priority-join"));
        joinsLeft--;
        if (joinsLeft <= 0) {
            revoke();
        }
    }

    @Override
    public void onRegister() {
        joinsLeft = getObject("joins_left", Integer.class);
    }

    @Override
    public void onPurchase() {
        setObject("joins_left", 50);
    }
}
