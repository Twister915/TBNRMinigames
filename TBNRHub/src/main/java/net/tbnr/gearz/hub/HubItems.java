package net.tbnr.gearz.hub;

import net.tbnr.gearz.hub.items.HubItem;
import net.tbnr.gearz.hub.items.warpstar.WarpStar;
import net.tbnr.gearz.hub.items.warpstar.WarpStarConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by rigor789 on 2013.12.21..
 */
public class HubItems implements Listener {

    private final ArrayList<HubItem> items;

    private final WarpStarConfig warpStarConfig;

    public HubItems() {
        items = new ArrayList<>();
        //items.add(new RuleBook());
        warpStarConfig = new WarpStarConfig();
        items.add(new WarpStar(warpStarConfig));
    }

    public void refreshWarpStar() {
        warpStarConfig.refresh();
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (HubItem item : items) {
            if (shouldAdd(event.getPlayer(), item.getItem())) event.getPlayer().getInventory().addItem(item.getItem());
        }
    }

    private boolean shouldAdd(Player player, ItemStack item) {
        return !player.getInventory().contains(item);
    }
}
