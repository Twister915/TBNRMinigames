package net.tbnr.announcer;

import lombok.Getter;
import net.tbnr.announcer.announces.AnnouncementStorage;
import net.tbnr.announcer.announces.AnnouncerManager;
import net.tbnr.util.TPlugin;

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
