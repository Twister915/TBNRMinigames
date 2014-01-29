package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;

@CommerceItemMeta(
        key = "book_of_effects",
        tier = Tier.Golden_Veteran,
        humanName = "Book Of Effects!"
)
public final class BookOfEffects extends CommerceItem {
    public BookOfEffects(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
