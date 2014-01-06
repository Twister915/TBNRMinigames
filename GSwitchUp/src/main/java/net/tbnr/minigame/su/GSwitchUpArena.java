package net.tbnr.minigame.su;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import org.bukkit.World;

/**
 * Created by Joey on 1/3/14.
 */
public class GSwitchUpArena extends Arena {
    public GSwitchUpArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public GSwitchUpArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }

    @ArenaField(longName = "Spawn Points", key = "spawn-points", loop = true, type = ArenaField.PointType.Player)
    public ArenaIterator<Point> spawnPoints;
}
