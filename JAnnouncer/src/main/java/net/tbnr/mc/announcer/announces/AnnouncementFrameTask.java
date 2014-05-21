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

package net.tbnr.mc.announcer.announces;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cogzmc.engine.gearz.effects.EnderBar;
import net.cogzmc.engine.gearz.player.GearzPlayer;
import net.tbnr.mc.announcer.JAnnouncer;
import net.tbnr.mc.announcer.effects.AnnouncementEffect;
import net.tbnr.mc.manager.TBNRNetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@EqualsAndHashCode(callSuper = false)
@Data
public class AnnouncementFrameTask extends BukkitRunnable {
    private final Announcement announcement;
    private final AnnouncementEffect effect;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            GearzPlayer gPlayer = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(player);
            EnderBar.setHealthPercent(gPlayer, JAnnouncer.getInstance().getAnnouncerManager().getHealthPercent(this, gPlayer));
            EnderBar.setTextFor(gPlayer, effect.getText(announcement, gPlayer));
        }
        JAnnouncer.getInstance().getAnnouncerManager().announcementTaskComplete(this);
    }
}
