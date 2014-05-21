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
import org.bukkit.Sound;

@CommerceItemMeta(
        humanName = "Shepherd",
        key = "shepherd",
        tier = Tier.Golden_Veteran,
        item = Material.WOOL
)
public final class Shepherd extends AbstractSoundItem {
    public Shepherd(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @Override
    protected Sound getSound() {
        return Sound.SHEEP_IDLE;
    }

}
