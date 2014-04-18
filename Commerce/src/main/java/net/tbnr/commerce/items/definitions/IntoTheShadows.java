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

package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.packets.wrapper.WrapperPlayServerWorldParticles;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.util.player.TPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@CommerceItemMeta(
        humanName = "Into The Shadows",
        key = "into_the_shadows",
        tier = Tier.Diamond_Veteran,
        item = Material.INK_SACK
)
public final class IntoTheShadows extends AbstractDeathItem {
    public IntoTheShadows(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    protected void performDeathAction() {
        Location location = getPlayer().getPlayer().getLocation();
        Set<GearzPlayer> players = new HashSet<>();
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(location) < 900) players.add(resolveTbnrPlayer(player));
        }
        for (GearzPlayer player : players) {
            try {
                player.getTPlayer().playParticleEffect(
                        new TPlayer.TParticleEffect(
                                location,
                                3,
                                4,
                                4,
                                4,
                                WrapperPlayServerWorldParticles.ParticleEffect.SMOKE
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
