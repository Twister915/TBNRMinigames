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

package net.tbnr.announcer.announces;

import java.util.List;

/**
 *
 */
public interface AnnouncementStorage {
    public List<Announcement> getAllAnnouncements();
    public void saveAnnouncement(Announcement announcement);
    public void removeAnnouncement(Announcement announcement);

    public Integer getInterval();
    public void setInterval(Integer interval);
}
