/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.gearz.hub.items;

import net.tbnr.gearz.hub.HubItem;
import net.tbnr.gearz.hub.HubItemMeta;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 17/02/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
@HubItemMeta(
		key = "pass",
		slot = 6
)
public class PassItem extends HubItem {

	public PassItem() {
		super(false);
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> items = new ArrayList<>();
		ItemStack itemStack = new ItemStack(Material.NAME_TAG, 1);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(getProperty("name", true));
		itemStack.setItemMeta(meta);
		items.add(itemStack);
		return items;
	}
}
