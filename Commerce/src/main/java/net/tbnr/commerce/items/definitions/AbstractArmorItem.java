/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractArmorItem extends CommerceItem {
    public AbstractArmorItem(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    protected abstract ItemStack[] armorContents();

    @Override
    public void onRegister() {
        if (!Gearz.getInstance().isLobbyServer()) return;
        getPlayer().getPlayer().getInventory().setArmorContents(armorContents());
    }
}
