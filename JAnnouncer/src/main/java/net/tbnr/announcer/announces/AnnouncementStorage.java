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
