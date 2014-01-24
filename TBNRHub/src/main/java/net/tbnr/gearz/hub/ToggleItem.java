package net.tbnr.gearz.hub;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 9/29/13
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unused")
public abstract class ToggleItem implements Listener {

    private Player player;
    private String title;
    private String[] lore;
    private boolean status;

    protected abstract boolean activate();

    protected abstract boolean deactivate();

    public ToggleItem(Player player, String title, String[] lore, boolean statusDefault) {
        this.player = player;
        this.title = title;
        this.lore = lore;
        this.status = statusDefault;
    }

    private short getDataValue() {
        return (short) (status ? 1 : 10);
    }

    /*protected void addToInventory() {
        TPlayerManager.getInstance().getPlayer(player).giveItem(Material.INK_SACK, 1, getDataValue(), this.title, this.lore);
    }*/
    /*private boolean shouldAdd() {
        while (this.player.getInventory().iterator().hasNext()) {
            ItemStack next = this.player.getInventory().iterator().next();
            if (next.getItemMeta().getDisplayName().equals(this.title) && next.getDurability() == getDataValue()) return false;
        }
        return true;
    }*/
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().equals(player)) return;
        if (!(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR))
            return;
        if (!(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(title))) return;

    }
}
