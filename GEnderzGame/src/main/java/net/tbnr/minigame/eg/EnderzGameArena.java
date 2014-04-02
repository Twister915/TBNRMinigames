package net.tbnr.minigame.eg;

import net.tbnr.gearz.arena.*;
import org.bukkit.World;

/**
 * Created by Joey on 12/31/13.
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public final class EnderzGameArena extends Arena {
    public EnderzGameArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public EnderzGameArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }

    @ArenaField(type = ArenaField.PointType.Player, loop = true, key = "spawn-points", longName = "Spawn Points")
    public PointIterator spawnPoints;
    @ArenaField(type = ArenaField.PointType.Player, loop = true, key = "drop-points", longName = "Drop Points")
    public PointIterator dropPoints;
}
