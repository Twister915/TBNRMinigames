package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.event.EventHandler;

@CommerceItemMeta(
        humanName = "Into The Shdows",
        key = "into_the_shadows",
        tier = Tier.Diamond_Veteran
)
public final class IntoTheShadows extends CommerceItem {
    public IntoTheShadows(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
