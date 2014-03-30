package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerGameDeathEvent;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public abstract class AbstractDeathItem extends CommerceItem {
    public AbstractDeathItem(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGameDeath(PlayerGameDeathEvent event) {
        if (!event.getDead().equals(getPlayer())) return;
        performDeathAction();
    }

    protected abstract void performDeathAction();
}
