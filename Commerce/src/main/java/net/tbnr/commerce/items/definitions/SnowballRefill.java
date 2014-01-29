package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;

@CommerceItemMeta(
        tier = Tier.Standard,
        key = "snowball_refill",
        humanName = "256x Snowballs in Hub"
)
public final class SnowballRefill extends CommerceItem {
    public SnowballRefill(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
