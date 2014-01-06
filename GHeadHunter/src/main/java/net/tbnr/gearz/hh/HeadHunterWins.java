package net.tbnr.gearz.hh;

import net.tbnr.util.player.TPlayerStorable;

/**
 * Created by Joey on 12/15/13.
 */
public class HeadHunterWins implements TPlayerStorable {
    private Integer wins;

    public HeadHunterWins(Integer wins) {
        this.wins = wins;
    }

    @Override
    public String getName() {
        return "wins";
    }

    @Override
    public Object getValue() {
        return this.wins;
    }

    public static HeadHunterWins get(Integer wins) {
        return new HeadHunterWins(wins);
    }
}
