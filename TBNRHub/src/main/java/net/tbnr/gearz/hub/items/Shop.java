package net.tbnr.gearz.hub.items;

import net.tbnr.gearz.hub.HubItem;
import net.tbnr.gearz.hub.HubItemMeta;
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
		key = "shop"
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
		player.chat("/shop");
	}

	@Override
	public void leftClicked(Player player) {
		rightClicked(player);
	}
}
