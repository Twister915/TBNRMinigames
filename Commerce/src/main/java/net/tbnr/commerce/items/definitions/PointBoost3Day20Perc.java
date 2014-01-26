package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerPointChangeEvent;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.player.TPlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

@CommerceItemMeta(
        humanName = "20% Point Boost (3days)",
        key = "3_20_point_boost",
        tier = Tier.Standard
)
public final class PointBoost3Day20Perc extends CommerceItem {
    private final String message;
    public PointBoost3Day20Perc(GearzPlayer player) throws GearzException {
        super(player);
        this.message = GearzCommerce.getInstance().getFormat("point-boost-message", false, new String[]{"<perc>", "30"});
    }

    @EventHandler
    public void onPointGain(PlayerPointChangeEvent event) {
        if (!event.getPlayer().equals(this.getPlayer())) return;
        TPlayer tPlayer = event.getPlayer().getTPlayer();
        tPlayer.sendMessage(this.message);
        tPlayer.playSound(Sound.ORB_PICKUP);
        event.setPoints(Double.valueOf(Math.ceil(event.getPoints() * 1.2d)).intValue());
    }
}
