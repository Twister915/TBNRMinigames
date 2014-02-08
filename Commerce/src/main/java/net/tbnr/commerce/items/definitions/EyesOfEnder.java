package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;

@CommerceItemMeta(
        humanName = "Eyes Of Ender",
        key = "eyes_of_ender",
        tier = Tier.Awesome
)
@Deprecated
public final class EyesOfEnder extends CommerceItem {
    public EyesOfEnder(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
