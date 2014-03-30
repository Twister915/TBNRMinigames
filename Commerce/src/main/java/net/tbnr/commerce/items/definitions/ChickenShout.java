package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;


@CommerceItemMeta(
        humanName = "Chicken Shout",
        tier = Tier.Golden_Veteran,
        key = "chicken_shout",
        item = Material.EGG
)
public final class ChickenShout extends AbstractSoundItem {
    public ChickenShout(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    protected Sound getSound() {
        return Sound.CHICKEN_HURT;
    }
}
