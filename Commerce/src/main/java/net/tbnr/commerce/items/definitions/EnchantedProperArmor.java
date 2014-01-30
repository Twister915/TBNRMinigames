package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.inventory.ItemStack;

@CommerceItemMeta(
        humanName = "Enchanted Proper Armor",
        key = "enchanted_proper_armor",
        tier = Tier.Awesome
)
public final class EnchantedProperArmor extends AbstractArmorItem {
    public EnchantedProperArmor(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    protected ItemStack[] armorContents() {
        return new ItemStack[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
