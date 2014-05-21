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

package net.tbnr.mc.announcer;

import lombok.Getter;
import net.cogzmc.engine.util.TPlugin;
import net.tbnr.mc.announcer.announces.AnnouncementStorage;
import net.tbnr.mc.announcer.announces.AnnouncerManager;

/**
 *
 * <p>
 * Latest Change:
 * <p>
 * @author Joey
 * @since 2/16/14
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
