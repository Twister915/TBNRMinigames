package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRPlayer;

@CommerceItemMeta(
        tier = Tier.Epic,
        key = "firework_creeper",
        humanName = "Firework + Creeper"
)
@Deprecated
public final class FireworkCreeper extends CommerceItem {
    public FireworkCreeper(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
