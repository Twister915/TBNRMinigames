package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;

@CommerceItemMeta(
        tier = Tier.Awesome,
        key = "enchanted_color_armor",
        humanName = "Enchanted Colored Armor"
)
public final class EnchantedColorArmor extends CommerceItem {
    public EnchantedColorArmor(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
