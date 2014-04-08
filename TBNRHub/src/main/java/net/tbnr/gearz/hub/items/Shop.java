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

import net.tbnr.gearz.hub.annotations.HubItem;
import net.tbnr.gearz.hub.annotations.HubItemMeta;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rigor789 on 2014.02.09..
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
@HubItemMeta(
		key = "shop",
		slot = 8
)
public class Shop extends HubItem {

	public Shop() {
		super(true);
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> items = new ArrayList<>();
		ItemStack itemStack = new ItemStack(Material.ANVIL, 1);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(getProperty("name", true));
		itemStack.setItemMeta(meta);
		items.add(itemStack);
		return items;
	}

	@Override
	public void rightClicked(Player player) {
        Bukkit.dispatchCommand(player, "shop");
	}

	@Override
	public void leftClicked(Player player) {
		rightClicked(player);
	}
}
