package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.player.TPlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractArmorItem extends CommerceItem {
    public AbstractArmorItem(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    protected abstract ItemStack[] armorContents();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(TPlayerJoinEvent event) {
        if (!Gearz.getInstance().isLobbyServer()) return;
        if (!event.getPlayer().equals(getPlayer().getTPlayer())) return;
        event.getPlayer().getPlayer().getInventory().setArmorContents(armorContents());
    }
}
