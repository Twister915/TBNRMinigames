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
import net.tbnr.mc.commerce.items.CommerceItem;
import net.tbnr.mc.commerce.items.CommerceItemAPI;
import net.tbnr.mc.commerce.items.CommerceItemMeta;
import net.tbnr.mc.commerce.items.Tier;
import net.tbnr.mc.manager.TBNRPlayer;

@CommerceItemMeta(
        tier = Tier.Epic,
        key = "firework_sparkle",
        humanName = "Firework Sparkle"
)
@Deprecated
public final class FireworkSparkle extends CommerceItem {
    public FireworkSparkle(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }
}
