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

package net.tbnr.manager;

import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.event.PlayerChangeDonorPointsEvent;
import net.tbnr.manager.event.PlayerLevelChangeEvent;
import net.tbnr.manager.event.PlayerPointChangeEvent;
import net.tbnr.manager.event.PlayerXPChangeEvent;
import net.tbnr.util.player.TPlayer;
import net.tbnr.util.player.TPlayerStorable;
import org.bukkit.Bukkit;

public final class TBNRPlayer extends GearzPlayer {
    public TBNRPlayer(TPlayer player) {
        super(player);
    }
    private boolean hideStats;
    private static final Integer magic_number = 7;
    @SuppressWarnings("unused")
    public void toggleStats() {
        setHideStats(!hideStats);
    }

    public void setHideStats(boolean h) {
        if (this.getPlayer() == null) {
            return;
        }
        this.hideStats = h;
        if (tPlayer.getPlayer() == null) {
            return;
        }
        if (!tPlayer.getPlayer().isOnline()) {
            return;
        }
        if (this.hideStats) {
            this.tPlayer.getPlayer().setExp(0);
            this.tPlayer.getPlayer().setLevel(0);
            this.tPlayer.getPlayer().setTotalExperience(0);
            this.tPlayer.resetScoreboard();
        } else {
            this.tPlayer.resetScoreboard();
            this.setupScoreboard();
            Bukkit.getScheduler().runTaskLater(TBNRNetworkManager.getInstance(), new Runnable() {
                @Override
                public void run() {
                    updateStats();
                }
            }, 5L);
        }
    }

    @SuppressWarnings("unused")
    public boolean areStatsHidden() {
        return this.hideStats;
    }

    public void addXp(int xp) {
        Integer current_xp = getXP();
        Integer newXp = Math.max(0, current_xp + xp);
        this.tPlayer.store(Gearz.getInstance(), new GPlayerXP(newXp));
        Bukkit.getPluginManager().callEvent(new PlayerXPChangeEvent(current_xp, newXp, this));
        this.updateStats();
    }

    public void addPoints(int points) {
        Integer current_points = getPoints();
        Integer newPoints = Math.max(0, current_points + points);
        PlayerPointChangeEvent tPlayerPointChangeEvent = new PlayerPointChangeEvent(this, current_points, newPoints, points);
        Bukkit.getPluginManager().callEvent(tPlayerPointChangeEvent);
        if (tPlayerPointChangeEvent.isCancelled()) {
            return;
        }
        if (tPlayerPointChangeEvent.getPoints() != points) newPoints = Math.max(0, current_points + tPlayerPointChangeEvent.getPoints());
        this.tPlayer.store(Gearz.getInstance(), new GPlayerPoints(newPoints));
        this.updateStats();
    }

    @SuppressWarnings("unused")
    public void addDonorPoint(int points) {
        Integer current_points = getDonorPoints();
        Integer newPoint = Math.max(0, current_points + points);
        this.tPlayer.store(Gearz.getInstance(), new GPlayerDonorPoints(newPoint));
        Bukkit.getPluginManager().callEvent(new PlayerChangeDonorPointsEvent(current_points, newPoint, this));
        this.updateStats();
    }

    private void setLevel(int level) {
        this.tPlayer.store(Gearz.getInstance(), new GPlayerLevel(level));
    }

    public Integer getLevel() {
        Integer storable = (Integer) this.tPlayer.getStorable(Gearz.getInstance(), "gearz-level");
        if (storable == null) {
            storable = 0;
        }
        return storable;
    }

    public Integer getPoints() {
        Integer storable = (Integer) this.tPlayer.getStorable(Gearz.getInstance(), "gearz-points");
        if (storable == null) {
            storable = 0;
        }
        return storable;
    }

    public Integer getXP() {
        Integer storable = (Integer) this.tPlayer.getStorable(Gearz.getInstance(), "gearz-xp");
        if (storable == null) {
            storable = 0;
        }
        return storable;
    }

    public Integer getDonorPoints() {
        Integer donorPoints = (Integer) this.tPlayer.getStorable(Gearz.getInstance(), "gearz-dpoints");
        if (donorPoints == null) {
            donorPoints = 0;
        }
        return donorPoints;
    }

    public String getYoutubeChannel() {
        return (String) this.tPlayer.getStorable(TBNRNetworkManager.getInstance(), "youtube");
    }

    public void setYoutubeChannel(String youtubeChannel) {
        this.tPlayer.store(TBNRNetworkManager.getInstance(), new GPlayerYoutube(youtubeChannel));
    }

    public void updateStats() {
        Integer xp = this.getXP();
        int new_level = this.getLevelFromXP(xp);
        Integer level = this.getLevel();
        if (level != new_level) {
            Bukkit.getPluginManager().callEvent(new PlayerLevelChangeEvent(level, new_level, this));
            this.setLevel(new_level);
            level = new_level;
        }
        if (this.hideStats) {
            return;
        }
        if (this.tPlayer.getPlayer() == null) {
            return;
        }
        this.tPlayer.getPlayer().setLevel(level);
        this.tPlayer.getPlayer().setExp(this.getProgressTowardsLevel(xp));
        TBNRNetworkManager instance = TBNRNetworkManager.getInstance();
        this.tPlayer.setScoreboardSideTitle(instance.getFormat("formats.sidebar-title"));
        this.tPlayer.setScoreBoardSide(instance.getFormat("formats.xp-sidebar"), getXP());
        this.tPlayer.setScoreBoardSide(instance.getFormat("formats.donor-points-sidebar"), getDonorPoints());
        this.tPlayer.setScoreBoardSide(instance.getFormat("formats.points-sidebar"), getPoints());
        this.tPlayer.setScoreBoardSide(instance.getFormat("formats.level-sidebar"), getLevel());
    }

    public void setupScoreboard() {
        TBNRNetworkManager instance = TBNRNetworkManager.getInstance();
        this.tPlayer.setScoreboardSideTitle(instance.getFormat("formats.sidebar-title-loading"));
        this.tPlayer.setScoreBoardSide(instance.getFormat("formats.xp-sidebar"), -1);
        this.tPlayer.setScoreBoardSide(instance.getFormat("formats.donor-points-sidebar"), -2);
        this.tPlayer.setScoreBoardSide(instance.getFormat("formats.points-sidebar"), -3);
        this.tPlayer.setScoreBoardSide(instance.getFormat("formats.level-sidebar"), -4);
    }

    private Integer getLevelFromXP(int xp) {
        if (xp < 0) {
            return 0;
        }
        return (int) Math.floor(Math.sqrt(xp) / TBNRPlayer.magic_number);
    }

    private float getProgressTowardsLevel(int xp) {
        return (float) ((Math.sqrt(xp) / TBNRPlayer.magic_number) % 1f);
    }

    public static class GPlayerXP implements TPlayerStorable {
        private final Integer xp;

        public GPlayerXP(Integer xp) {
            this.xp = xp;
        }

        @Override
        public String getName() {
            return "gearz-xp";
        }

        @Override
        public Object getValue() {
            return xp;
        }
    }

    public static class GPlayerLevel implements TPlayerStorable {
        private final Integer level;

        public GPlayerLevel(Integer level) {
            this.level = level;
        }

        @Override
        public String getName() {
            return "gearz-level";
        }

        @Override
        public Object getValue() {
            return level;
        }
    }

    public static class GPlayerPoints implements TPlayerStorable {
        private final Integer points;

        public GPlayerPoints(Integer points) {
            this.points = points;
        }

        @Override
        public String getName() {
            return "gearz-points";
        }

        @Override
        public Object getValue() {
            return points;
        }
    }

    @Override
    public TPlayer getTPlayer() {
        return this.tPlayer;
    }

    public static class GPlayerDonorPoints implements TPlayerStorable {
        private final Integer points;

        public GPlayerDonorPoints(Integer newPoint) {
            points = newPoint;
        }

        @Override
        public String getName() {
            return "gearz-dpoints";
        }

        @Override
        public Object getValue() {
            return points;
        }
    }

    public static class GPlayerYoutube implements TPlayerStorable {
        private final String youtube;

        public GPlayerYoutube(String youtube) {
            this.youtube = youtube;
        }

        @Override
        public String getName() {
            return "youtube";
        }

        @Override
        public Object getValue() {
            return youtube;
        }
    }
}
