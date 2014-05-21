/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.mc.commerce.items.definitions;

import net.cogzmc.engine.gearz.GearzException;
import net.tbnr.mc.commerce.items.CommerceItemAPI;
import net.tbnr.mc.commerce.items.CommerceItemMeta;
import net.tbnr.mc.commerce.items.Tier;
import net.tbnr.mc.manager.TBNRPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

@CommerceItemMeta(
        humanName = "Death Is A Celebration",
        key = "death_is_a_celebration",
        tier = Tier.Diamond_Veteran,
        item = Material.FIREWORK
)
public final class DeathIsACelebration extends AbstractDeathItem {
    public DeathIsACelebration(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
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
