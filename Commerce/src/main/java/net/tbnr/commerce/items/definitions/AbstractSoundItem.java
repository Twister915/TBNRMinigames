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
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.player.PlayerGameDamageByPlayerEvent;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public abstract class AbstractSoundItem extends CommerceItem {
    public AbstractSoundItem(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    protected abstract Sound getSound();

    @EventHandler
    public void onPlayerDamage(PlayerGameDamageByPlayerEvent event) {
        if (!event.getPlayer().equals(getPlayer())) return;
        Sound sound = getSound();
        event.getDamager().getTPlayer().playSound(sound);
        event.getPlayer().getTPlayer().playSound(sound);
    }
}
