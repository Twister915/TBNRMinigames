package net.tbnr.commerce.items.definitions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public final class ArmorSet {
    private final Material hat;
    private final Material chestplate;
    private final Material leggings;
    private final Material boots;

    public ItemStack[] getArmorSet() {
        return new ItemStack[] {new ItemStack(boots), new ItemStack(leggings), new ItemStack(chestplate), new ItemStack(hat)};
    }

}
