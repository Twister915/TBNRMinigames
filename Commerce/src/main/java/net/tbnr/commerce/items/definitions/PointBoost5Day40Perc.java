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
public final class PointBoost5Day40Perc extends CommerceItem {
    private final String message;
    public PointBoost5Day40Perc(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
        this.message = GearzCommerce.getInstance().getFormat("point-boost-message", false, new String[]{"<perc>", "40"});
    }

    @EventHandler
    public void onPointGain(PlayerPointChangeEvent event) {
        if (!event.getPlayer().equals(this.getPlayer())) return;
        TPlayer tPlayer = event.getPlayer().getTPlayer();
        tPlayer.sendMessage(this.message);
        tPlayer.playSound(Sound.ORB_PICKUP);
        event.setPoints(Double.valueOf(Math.ceil(event.getPoints() * 1.4d)).intValue());
    }
}
