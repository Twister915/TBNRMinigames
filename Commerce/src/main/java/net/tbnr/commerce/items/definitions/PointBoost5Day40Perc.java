package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerPointChangeEvent;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.player.TPlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

@CommerceItemMeta(
        tier = Tier.Heroic,
        humanName = "40% Point Boost (5 Days)",
        key = "5_40_point_boost"
)
public final class PointBoost5Day40Perc extends PointBoost{
    public PointBoost5Day40Perc(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    public int percentageBoost() {
        return 40;
    }

    @Override
    public int daysLength() {
        return 5;
    }
}
