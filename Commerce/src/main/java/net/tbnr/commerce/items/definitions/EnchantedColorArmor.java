package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@CommerceItemMeta(
        tier = Tier.Awesome,
        key = "enchanted_color_armor",
        humanName = "Enchanted Colored Armor",
        item = Material.LEATHER_CHESTPLATE
)
public class EnchantedColorArmor extends ColoredArmor {
    public EnchantedColorArmor(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    protected ItemStack[] armorContents() {
        ItemStack[] itemStacks = super.armorContents();
        for (ItemStack itemStack : itemStacks) {
            itemStack.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 32);
        }
        return itemStacks;
    }
}
