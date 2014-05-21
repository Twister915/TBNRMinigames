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

import net.cogzmc.engine.gearz.Gearz;
import net.cogzmc.engine.gearz.GearzException;
import net.cogzmc.engine.util.player.TPlayer;
import net.tbnr.mc.commerce.GearzCommerce;
import net.tbnr.mc.commerce.items.CommerceItem;
import net.tbnr.mc.commerce.items.CommerceItemAPI;
import net.tbnr.mc.commerce.items.CommerceItemMeta;
import net.tbnr.mc.commerce.items.Tier;
import net.tbnr.mc.manager.TBNRPlayer;
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
