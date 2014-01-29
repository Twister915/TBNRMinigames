package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;

@CommerceItemMeta(
        humanName = "Death Is A Celebration",
        key = "death_is_a_celebration",
        tier = Tier.Diamond_Veteran
)
public final class DeathIsACelebration extends CommerceItem{
    public DeathIsACelebration(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
