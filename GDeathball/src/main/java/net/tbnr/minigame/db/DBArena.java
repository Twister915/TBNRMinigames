package net.tbnr.minigame.db;

import net.tbnr.gearz.arena.*;
import org.bukkit.World;

/**
 * Created by rigor789 on 2013.12.19..
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final class DBArena extends Arena {

    @ArenaField(longName = "Spawn Points", key = "spawn-points", loop = true, type = ArenaField.PointType.Player)
    public PointIterator spawnPoints;

    public DBArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public DBArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }
}
