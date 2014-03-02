package net.tbnr.gearz.hub.votifier;

import lombok.Data;
import lombok.Getter;
import net.tbnr.util.player.TPlayerStorable;

@Data
public class TimesVoted implements TPlayerStorable {
    private final Integer value;
    @Getter private static final String staticName  = "times_voted";
    private final String name = staticName;
}
