package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;

@CommerceItemMeta(
        humanName = "Firework - Everything",
        key = "firework_everything",
        tier = Tier.Iron_Veteran
)
@Deprecated
public final class FireworkEverything extends CommerceItem {
    public FireworkEverything(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
