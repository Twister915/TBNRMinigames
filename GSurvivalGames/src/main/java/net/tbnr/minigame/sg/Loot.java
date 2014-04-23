/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

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
public final class Loot {
    @NonNull
    final Tier tier;
    @NonNull
    final Chest chest;

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
