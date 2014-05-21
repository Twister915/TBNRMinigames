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
import net.cogzmc.engine.gearz.event.player.PlayerPriorityDetermineEvent;
import net.tbnr.mc.commerce.GearzCommerce;
import net.tbnr.mc.commerce.items.CommerceItem;
import net.tbnr.mc.commerce.items.CommerceItemAPI;
import net.tbnr.mc.commerce.items.CommerceItemMeta;
import net.tbnr.mc.commerce.items.Tier;
import net.tbnr.mc.manager.TBNRPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

@CommerceItemMeta(
        tier = Tier.Standard,
        key = "fifty_premium_joins",
        humanName = "50x Premium Joins",
        item = Material.PAPER
)
public final class FiftyPremiumJoins extends CommerceItem {
    
    private Integer joinsLeft;

    public FiftyPremiumJoins(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @EventHandler
    public void onPlayerJoin(PlayerPriorityDetermineEvent event) {
        if (!event.getPlayer().equals(getPlayer())) return;
        event.setAbsolutePriority(true);
        event.getPlayer().getTPlayer().sendMessage(GearzCommerce.getInstance().getFormat("formats.priority-join"));
        joinsLeft--;
        if (joinsLeft <= 0) {
            revoke();
        }
    }

    @Override
    public void onRegister() {
        joinsLeft = getObject("joins_left", Integer.class);
    }

    @Override
    public void onPurchase() {
        setObject("joins_left", 50);
    }
}
