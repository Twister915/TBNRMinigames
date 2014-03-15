package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.Material;

@CommerceItemMeta(
        humanName = "Spontaneous Combustion",
        key = "spontaneous_combustion",
        tier = Tier.Iron_Veteran,
        item = Material.TNT
)
public final class SpontaneousCombustion extends CommerceItem {
    public SpontaneousCombustion(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
