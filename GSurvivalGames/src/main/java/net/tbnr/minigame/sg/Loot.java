package net.tbnr.minigame.sg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import net.tbnr.gearz.Gearz;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@EqualsAndHashCode
public class Loot {
    @NonNull Tier tier;
    @NonNull Chest chest;

    public void fillChest() {
        List<ItemStack> items = tier.getItems();
        Inventory inventory = chest.getInventory();
        inventory.clear();
        for (ItemStack stack : items) {
            int slot = 0;
            while (inventory.getItem(slot) != null) {
                slot = Gearz.getRandom().nextInt(inventory.getSize());
            }
            inventory.setItem(slot, stack);
        }
    }
}
