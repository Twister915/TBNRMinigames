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

package net.tbnr.mc.commerce.items.definitions;

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
