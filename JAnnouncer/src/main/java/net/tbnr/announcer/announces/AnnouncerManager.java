package net.tbnr.announcer.announces;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.tbnr.announcer.JAnnouncer;
import net.tbnr.announcer.effects.AnnouncementEffect;
import net.tbnr.announcer.effects.definitions.BouncingBold;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.player.GearzPlayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public final class AnnouncerManager implements AnnouncementStorageDelegate {

    private final AnnouncementStorage storage;

    private Integer announcementIndex;
    private Integer effectIndex;
    private Integer runFrames;

    private final static Integer effectRateTicks = 3;
    private Integer announcementSeconds;

    private List<Announcement> bufferedAnnouncementList = null;

    private static List<Class<? extends AnnouncementEffect>> announcementEffects = new ArrayList<>();

    static {
        announcementEffects.addAll(Arrays.asList(
                BouncingBold.class
        ));
    }

    public void start() {
        if (bufferedAnnouncementList == null) reload();
        moveOn();
    }

    private static AnnouncementEffect constructEffectInstance(Class<? extends AnnouncementEffect> effectClass) throws EffectConstructorException {
        Constructor<? extends AnnouncementEffect> constructor;
        try {
            constructor = effectClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new EffectConstructorException("Could not get the constructor", e);
        }
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new EffectConstructorException("Could not create a new instance!", e);
        }
    }

    public void stop() {
        bufferedAnnouncementList = null;
    }

    public void reload() {
        stop();
        announcementSeconds = storage.getInterval();
        bufferedAnnouncementList = storage.getAllAnnouncements();
        announcementIndex = 0;
        effectIndex = 0;
        runFrames = 0;
    }

    public float getHealthPercent(AnnouncementFrameTask task, GearzPlayer player) {
        return 1;
    }

    public void announcementTaskComplete(AnnouncementFrameTask task) {
        if (bufferedAnnouncementList == null) return;
        moveOn();
    }

    private void startAnnouncementTask(Announcement announcement, AnnouncementEffect effect) {
        AnnouncementFrameTask frameTask = new AnnouncementFrameTask(announcement, effect);
        frameTask.runTaskLater(JAnnouncer.getInstance(), effectRateTicks);
        runFrames++;
        if ((runFrames*effectRateTicks)/20 >= announcementSeconds) {
            runFrames = 0;
            effectIndex = Gearz.getRandom().nextInt(announcementEffects.size());
            announcementIndex++;
            if (announcementIndex >= bufferedAnnouncementList.size()) announcementIndex = 0;
        }
    }

    private void moveOn() {
        try {
            startAnnouncementTask(bufferedAnnouncementList.get(announcementIndex), constructEffectInstance(announcementEffects.get(effectIndex)));
        } catch (EffectConstructorException e) {
            e.printStackTrace();
        }
    }

    @EqualsAndHashCode(callSuper = false)
    @Data
    private static final class EffectConstructorException extends Exception {
        private final String message;
        private final Exception cause;
    }
}
