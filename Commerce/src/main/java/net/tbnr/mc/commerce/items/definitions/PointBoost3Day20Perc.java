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
import org.bukkit.Material;

@CommerceItemMeta(
        humanName = "20% Point Boost (3 Days)",
        key = "3_20_point_boost",
        tier = Tier.Standard,
        item = Material.POTION
)
public final class PointBoost3Day20Perc extends AbstractPointBoost {
    public PointBoost3Day20Perc(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    public int percentageBoost() {
        return 20;
    }

    @Override
    public int daysLength() {
        return 3;
    }
}
