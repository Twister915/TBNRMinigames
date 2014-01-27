package net.tbnr.gearz.pl.GPlague;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import org.bukkit.World;

/**
 * Created by George on 27/01/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
public class PlagueArena extends Arena {

	@ArenaField(key = "spawn-points", loop = true, type = ArenaField.PointType.Player, longName = "Spawn Points")
	public ArenaIterator<Point> spawnPoints;

	public PlagueArena(String name, String author, String description, String worldId, String id) {
		super(name, author, description, worldId, id);
	}

	public PlagueArena(String name, String author, String description, World world) {
		super(name, author, description, world);
	}
}
