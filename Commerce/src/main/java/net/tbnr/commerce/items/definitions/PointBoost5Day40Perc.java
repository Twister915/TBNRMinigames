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
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.Material;

@CommerceItemMeta(
        tier = Tier.Heroic,
        humanName = "40% Point Boost (5 Days)",
        key = "5_40_point_boost",
        item = Material.POTION
)
public final class PointBoost5Day40Perc extends AbstractPointBoost {
    public PointBoost5Day40Perc(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    public int percentageBoost() {
        return 40;
    }

    @Override
    public int daysLength() {
        return 5;
    }
}
