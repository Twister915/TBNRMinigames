package net.tbnr.gearz.hub.items;

import net.tbnr.gearz.hub.HubItem;
import net.tbnr.gearz.hub.HubItemMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake on 1/16/14.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
@HubItemMeta(
        key = "magicclock"
)
public class MagicClock extends HubItem {
    final List<String> enabledFor = new ArrayList<>();

    public MagicClock() {
        super(true);
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.ITALIC + "Magic Star!" + ChatColor.GRAY + " - " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Click me!");
        itemStack.setItemMeta(meta);
        return itemStack;
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
        if (enabledFor.contains(player.getName())) {
            enabledFor.remove(player.getName());
        } else {
            enabledFor.add(player.getName());
        }
    }

    public void handleToggle(Player player) {
        if (enabledFor.contains(player.getName())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                player.showPlayer(online);
            }
        } else {
            for (Player online : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(online);
            }
        }
    }

    public boolean isEnabled(Player player) {
        return enabledFor.contains(player.getName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isEnabled(player)) player.hidePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String user = player.getName();
        if (enabledFor.contains(user)) {
            enabledFor.remove(user);
        }
    }
}
