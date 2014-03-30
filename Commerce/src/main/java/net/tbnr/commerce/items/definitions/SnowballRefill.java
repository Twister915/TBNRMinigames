package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.util.player.TPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;

@CommerceItemMeta(
        tier = Tier.Standard,
        key = "snowball_refill",
        humanName = "256x Snowballs in Hub",
        item = Material.SNOW_BALL
)
public final class SnowballRefill extends CommerceItem {
    public SnowballRefill(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    public void onRegister() {
        if (!getObject("has-given", Boolean.class) && Gearz.getInstance().isLobbyServer()) {
            TPlayer tPlayer = getPlayer().getTPlayer();
            tPlayer.giveItem(Material.SNOW_BALL, 256);
            tPlayer.sendMessage(GearzCommerce.getInstance().getFormat("formats.snowballs-delivered"));
            setObject("has-given", true);
            Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
                @Override
                public void run() {
                    revoke();
                }
            }, 1L);
        }
    }

    @Override
    public void onPurchase() {
        setObject("has-given", false);
    }
}
