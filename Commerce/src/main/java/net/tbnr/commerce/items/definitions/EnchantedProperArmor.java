package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@CommerceItemMeta(
        humanName = "Enchanted Proper Armor",
        key = "enchanted_proper_armor",
        tier = Tier.Awesome,
        item = Material.DIAMOND_CHESTPLATE
)
public final class EnchantedProperArmor extends EnchantedColorArmor {
    public EnchantedProperArmor(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
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
