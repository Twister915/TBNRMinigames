package net.tbnr.gearz.hh;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import org.bukkit.World;

/**
 * HeadHunter Arena
 */
public class HeadHunterArena extends Arena {

    @ArenaField(key = "spawn-points", loop = true, type = ArenaField.PointType.Player, longName = "Spawn Points")
    public ArenaIterator<Point> spawnPoints;

    public HeadHunterArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public HeadHunterArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }
}

