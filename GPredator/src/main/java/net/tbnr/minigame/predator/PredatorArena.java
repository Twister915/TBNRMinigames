package net.tbnr.minigame.predator;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import org.bukkit.World;

/**
 * Created by George on 11/01/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
public class PredatorArena extends Arena {
    @ArenaField(key = "predator-spawn", loop = true, type = ArenaField.PointType.Player, longName = "Predator Spawn")
    public ArenaIterator<Point> predatorSpawn;

    @ArenaField(key = "spawn-points", loop = true, type = ArenaField.PointType.Player, longName = "Spawn Points")
    public ArenaIterator<Point> spawnPoints;

    public PredatorArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public PredatorArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }
}
