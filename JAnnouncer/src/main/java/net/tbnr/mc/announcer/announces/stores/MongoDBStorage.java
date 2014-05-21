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

package net.tbnr.mc.announcer.announces.stores;

import net.tbnr.mc.announcer.announces.Announcement;
import net.tbnr.mc.announcer.announces.AnnouncementStorage;

import java.util.List;

/**
 *
 * <p>
 * Latest Change:
 * <p>
 * @author Joey
 * @since 2/16/14
 */
public class MongoDBStorage implements AnnouncementStorage {
    @Override
    public List<Announcement> getAllAnnouncements() {
        return null;
    }

    @Override
    public void saveAnnouncement(Announcement announcement) {

    }

    @Override
    public void removeAnnouncement(Announcement announcement) {

    }

    @Override
    public Integer getInterval() {
        return null;
    }

    @Override
    public void setInterval(Integer interval) {

    }
}
