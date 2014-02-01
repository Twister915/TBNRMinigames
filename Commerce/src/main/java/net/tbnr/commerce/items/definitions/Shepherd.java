package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.Sound;

@CommerceItemMeta(
        humanName = "Shepherd",
        key = "shepherd",
        tier = Tier.Golden_Veteran
)
public final class Shepherd extends AbstractSoundItem {
    public Shepherd(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    protected Sound getSound() {
        return Sound.SHEEP_IDLE;
    }

}
