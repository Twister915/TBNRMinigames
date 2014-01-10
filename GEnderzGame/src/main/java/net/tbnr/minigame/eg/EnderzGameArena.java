package net.tbnr.minigame.eg;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import org.bukkit.World;

/**
 * Created by Joey on 12/31/13.
 */
public final class EnderzGameArena extends Arena {
    public EnderzGameArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public EnderzGameArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }

    @ArenaField(type = ArenaField.PointType.Player, loop = true, key = "spawn-points", longName = "Spawn Points")
    public ArenaIterator<Point> spawnPoints;
    @ArenaField(type = ArenaField.PointType.Player, loop = true, key = "drop-points", longName = "Drop Points")
    public ArenaIterator<Point> dropPoints;
}
