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

package net.tbnr.announcer;

import lombok.Getter;
import net.tbnr.announcer.announces.AnnouncementStorage;
import net.tbnr.announcer.announces.AnnouncerManager;
import net.tbnr.util.TPlugin;

/**
 * Created by Joey on 2/16/14.
 */
public class JAnnouncer extends TPlugin {
    @Getter private static JAnnouncer instance;
    @Getter private AnnouncerManager announcerManager;

    @Override
    public void enable() {
        JAnnouncer.instance = this;
        AnnouncementStorage storage = null; //TODO create a storage instance
        announcerManager = new AnnouncerManager(storage);
        announcerManager.start();
    }

    @Override
    public void disable() {

    }

    @Override
    public String getStorablePrefix() {
        return "announcer";
    }
}
