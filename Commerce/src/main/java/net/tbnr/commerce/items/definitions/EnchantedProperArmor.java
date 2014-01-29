package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;

@CommerceItemMeta(
        humanName = "Enchanted Proper Armor",
        key = "enchanted_proper_armor",
        tier = Tier.Awesome
)
public final class EnchantedProperArmor extends CommerceItem {
    public EnchantedProperArmor(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
