package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;

@CommerceItemMeta(
        tier = Tier.Heroic,
        humanName = "40% Point Boost (5 Days)",
        key = "5_40_point_boost"
)
public final class PointBoost5Day40Perc extends AbstractPointBoost {
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
