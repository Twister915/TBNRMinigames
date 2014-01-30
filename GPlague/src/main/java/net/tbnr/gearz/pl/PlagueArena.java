package net.tbnr.gearz.pl;

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

	@ArenaField(key = "human-spawn", loop = true, type = ArenaField.PointType.Player, longName = "Human Spawn Points")
	public ArenaIterator<Point> humanSpawnPoints;

	@ArenaField(key = "zombie-spawn", loop = true, type = ArenaField.PointType.Player, longName = "Zombie Spawn Points")
	public ArenaIterator<Point> zombieSpawnPoints;


	public PlagueArena(String name, String author, String description, String worldId, String id) {
		super(name, author, description, worldId, id);
	}

	public PlagueArena(String name, String author, String description, World world) {
		super(name, author, description, world);
	}
}
