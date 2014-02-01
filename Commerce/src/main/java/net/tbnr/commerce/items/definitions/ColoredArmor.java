package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;

@CommerceItemMeta(
        key = "colored_armor",
        tier = Tier.Heroic,
        humanName = "Colored Armor"
)
public final class ColoredArmor extends CommerceItem {
    public ColoredArmor(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
