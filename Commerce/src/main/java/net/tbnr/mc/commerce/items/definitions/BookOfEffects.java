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
import net.tbnr.mc.commerce.items.CommerceItem;
import net.tbnr.mc.commerce.items.CommerceItemAPI;
import net.tbnr.mc.commerce.items.CommerceItemMeta;
import net.tbnr.mc.commerce.items.Tier;
import net.tbnr.mc.manager.TBNRPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

@CommerceItemMeta(
        key = "book_of_effects",
        tier = Tier.Golden_Veteran,
        humanName = "Book Of Effects!"
)
@Deprecated
public final class BookOfEffects extends CommerceItem {
    public BookOfEffects(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    /*
    Impl
     */

    @EventHandler(priority =  EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Gearz.getInstance().isLobbyServer()) return;
        TBNRPlayer player = resolveTbnrPlayer(event.getPlayer());
        //if (!this.getPlayer().equals(player)) return;
        //TPlayer tPlayer = player.getTPlayer();
        //TODO give the player a book, if they don't already have it.
    }

    //TODO Check if the player interacts with the book in the hub and display an inventory screen.
    //TODO link objects with potion effects

}
