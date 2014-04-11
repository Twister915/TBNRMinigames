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
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.manager.TBNRPlayer;

@CommerceItemMeta(
        humanName = "Firework - Everything",
        key = "firework_everything",
        tier = Tier.Iron_Veteran
)
@Deprecated
public final class FireworkEverything extends CommerceItem {
    public FireworkEverything(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
