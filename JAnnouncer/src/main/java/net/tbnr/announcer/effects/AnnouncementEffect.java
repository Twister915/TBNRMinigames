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

package net.tbnr.announcer.effects;

import net.tbnr.announcer.announces.Announcement;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.Bukkit;

public abstract class AnnouncementEffect {

    private String generatedFinalString = null;

    public String getText(Announcement announcement, GearzPlayer player) {
        while (generatedFinalString == null) {
            String message = announcement.getMessage();
            message = message.replaceAll("%player", player.getUsername());
            message = message.replaceAll("%rank", Gearz.getInstance().getPermissionsDelegate().getPrefix(player.getPlayer().getName()));
            message = message.replaceAll("%online", String.valueOf(Bukkit.getOnlinePlayers().length));
            message = message.replaceAll("%totalOnline", "0"); //TODO getOnline
            generatedFinalString = message;
        }
        /* actual text */
        return processText(generatedFinalString, player);
    }

    public void clearGeneratedString() {
        generatedFinalString = null;
    }

    protected abstract String processText(String finalText, GearzPlayer player);
}
