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
import net.cogzmc.engine.gearz.event.player.PlayerGameKillEvent;
import net.cogzmc.engine.gearz.packets.wrapper.WrapperPlayServerWorldParticles;
import net.cogzmc.engine.util.player.TPlayer;
import net.tbnr.mc.commerce.GearzCommerce;
import net.tbnr.mc.commerce.items.CommerceItem;
import net.tbnr.mc.commerce.items.CommerceItemAPI;
import net.tbnr.mc.commerce.items.CommerceItemMeta;
import net.tbnr.mc.commerce.items.Tier;
import net.tbnr.mc.manager.TBNRPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommerceItemMeta(
        key = "rose_of_death",
        humanName = "Romantic Death",
        tier = Tier.Diamond_Veteran,
        item = Material.RED_ROSE
)
public final class RoseOfDeath extends CommerceItem {
    public RoseOfDeath(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
    @EventHandler
    public void onDeath(PlayerGameKillEvent event) {
        if (!event.getDead().equals(this.getPlayer())) return;
        Location location = event.getDead().getPlayer().getLocation();
        ItemStack itemStack = new ItemStack(Material.RED_ROSE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(GearzCommerce.getInstance().getFormat("formats.rose-title"));
        itemStack.setItemMeta(itemMeta);
        event.getGame().getArena().getWorld().dropItem(location, itemStack);
        try {
            event.getKiller().getTPlayer().playParticleEffect(new TPlayer.TParticleEffect(
                    event.getDead().getPlayer().getLocation(),
                    1,
                    2,
                    2,
                    2,
                    WrapperPlayServerWorldParticles.ParticleEffect.HEART
            ));
        } catch (Exception ignored) {}
    }
}
