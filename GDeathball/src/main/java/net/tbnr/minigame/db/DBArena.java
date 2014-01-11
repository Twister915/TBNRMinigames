package net.tbnr.minigame.db;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import org.bukkit.World;

/**
 * Created by rigor789 on 2013.12.19..
 */
public final class DBArena extends Arena {

    @ArenaField(longName = "Spawn Points", key = "spawn-points", loop = true, type = ArenaField.PointType.Player)
    public ArenaIterator<Point> spawnPoints;

    public DBArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public DBArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }
}
