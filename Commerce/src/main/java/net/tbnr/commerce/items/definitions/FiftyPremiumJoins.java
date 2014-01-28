package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerPriorityDetermineEvent;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.event.EventHandler;

@CommerceItemMeta(
        tier = Tier.Standard,
        key = "fifty_premium_joins",
        humanName = "50x Premium Joins"
)
public final class FiftyPremiumJoins extends CommerceItem {
    public FiftyPremiumJoins(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
    @EventHandler
    public void onPlayerJoin(PlayerPriorityDetermineEvent event) {

    }

    @Override
    public void onPurchase() {
        setObjectInDB("joins_left", 50);
    }
}
