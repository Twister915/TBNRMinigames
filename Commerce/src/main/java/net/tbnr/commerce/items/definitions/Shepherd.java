package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerGameDamageByPlayerEvent;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

@CommerceItemMeta(
        humanName = "Shepherd",
        key = "shepherd",
        tier = Tier.Golden_Veteran
)
public final class Shepherd extends CommerceItem {
    public Shepherd(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @EventHandler
    public void onPlayerDamage(PlayerGameDamageByPlayerEvent event) {
        if (!event.getPlayer().equals(getPlayer())) return;
        event.getDamager().getTPlayer().playSound(Sound.SHEEP_IDLE);
        event.getPlayer().getTPlayer().playSound(Sound.SHEEP_IDLE);
    }
}
