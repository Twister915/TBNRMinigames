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

package net.tbnr.gearz.hub.items;

import net.tbnr.gearz.hub.TBNRHub;
import net.tbnr.gearz.hub.annotations.HubItem;
import net.tbnr.gearz.hub.annotations.HubItemMeta;
import net.tbnr.util.player.cooldowns.TCooldown;
import net.tbnr.util.player.cooldowns.TCooldownManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by George on 10/02/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
@HubItemMeta(
		key = "speedup",
		slot = 4
)
public class SpeedUp extends HubItem {
	final List<String> enabledFor = new ArrayList<>();

	public SpeedUp() {
		super(true);
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> items = new ArrayList<>();
		ItemStack itemStack = new ItemStack(Material.LEVER, 1);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(getProperty("name", true));
		itemStack.setItemMeta(meta);
		items.add(itemStack);

		ItemStack redstoneTorch = itemStack.clone();
		redstoneTorch.setType(Material.REDSTONE_TORCH_ON);
		items.add(redstoneTorch);
		return items;
	}

	@Override
	public void rightClicked(Player player) {
		toggle(player);
		handleToggle(player);
	}

	@Override
	public void leftClicked(Player player) {
		rightClicked(player);
	}

	public void toggle(Player player) {
		if(TCooldownManager.canContinueLocal(player.getName() + "_speedUp", new TCooldown(TimeUnit.SECONDS.toMillis(3)))) {
			if (enabledFor.contains(player.getName())) {
				player.sendMessage(getProperty("toggleOff", true, new String[]{"<prefix>", TBNRHub.getInstance().getChatPrefix()}));
				player.getItemInHand().setType(Material.LEVER);
				enabledFor.remove(player.getName());
			} else {
				player.sendMessage(getProperty("toggleOn", true, new String[]{"<prefix>", TBNRHub.getInstance().getChatPrefix()}));
				player.getItemInHand().setType(Material.REDSTONE_TORCH_ON);
				enabledFor.add(player.getName());
			}
			player.playSound(player.getLocation(), Sound.ARROW_HIT, 1, 1);
		} else {
			player.sendMessage(getProperty("cooldown", true, new String[]{"<prefix>", TBNRHub.getInstance().getChatPrefix()}));
		}
	}

	public void handleToggle(Player player) {
		if (!enabledFor.contains(player.getName())) {
			player.removePotionEffect(PotionEffectType.JUMP);
			player.removePotionEffect(PotionEffectType.SPEED);
			return;
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, getConfigurationSection().getInt("speed-level", 0)));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, getConfigurationSection().getInt("jump-level", 0)));

	}

	public boolean isEnabled(Player player) {
		return enabledFor.contains(player.getName());
	}
}
