package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.inventory.ItemStack;

@CommerceItemMeta(
        tier = Tier.Awesome,
        key = "enchanted_color_armor",
        humanName = "Enchanted Colored Armor"
)
public final class EnchantedColorArmor extends AbstractArmorItem {
    public EnchantedColorArmor(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    protected ItemStack[] armorContents() {
        return new ItemStack[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
