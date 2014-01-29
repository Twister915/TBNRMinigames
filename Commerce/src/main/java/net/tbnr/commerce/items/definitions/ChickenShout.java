package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;


@CommerceItemMeta(
        humanName = "Chicken Shout",
        tier = Tier.Golden_Veteran,
        key = "chicken_shout"
)
public final class ChickenShout extends CommerceItem {
    public ChickenShout(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
