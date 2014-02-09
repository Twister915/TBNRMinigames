package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

@CommerceItemMeta(
        humanName = "Death Is A Celebration",
        key = "death_is_a_celebration",
        tier = Tier.Diamond_Veteran
)
public final class DeathIsACelebration extends AbstractDeathItem {
    public DeathIsACelebration(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    protected void performDeathAction() {
        Location location = getPlayer().getPlayer().getLocation();
        Firework entity = (Firework)location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = entity.getFireworkMeta();
        fireworkMeta.addEffects(FireworkEffect.builder().
                trail(true).
                flicker(true).
                withColor(Color.ORANGE).
                withColor(Color.RED).
                withColor(Color.BLUE).
                withColor(Color.GREEN).
                with(FireworkEffect.Type.BALL_LARGE).
                withFade(Color.BLACK, Color.GRAY, Color.WHITE).
                build());
        entity.setFireworkMeta(fireworkMeta);
    }
}
