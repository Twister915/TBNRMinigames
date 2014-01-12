package net.tbnr.gearz.hub.items;

import net.tbnr.gearz.hub.TBNRHub;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by rigor789 on 2013.12.21..
 */
public abstract class HubItem implements Listener {
    public abstract ItemStack getItem();

    public void rightClicked(Player player) {
    }

    public void leftClicked(Player player) {
    }

    public HubItem(boolean interactable) {
        if (interactable) TBNRHub.getInstance().registerEvents(this);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public final void onInteract(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR))
            return;
        if (event.getPlayer().getItemInHand() == null) return;
        if (!event.getPlayer().getItemInHand().hasItemMeta()) return;
        if (!(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(getItem().getItemMeta().getDisplayName())))
            return;
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                rightClicked(event.getPlayer());
                break;
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                leftClicked(event.getPlayer());
                break;
        }
        event.setCancelled(true);
    }
}
