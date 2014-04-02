package net.tbnr.gearz.pl;

import net.tbnr.gearz.arena.*;
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
	public PointIterator humanSpawnPoints;

	@ArenaField(key = "zombie-spawn", loop = true, type = ArenaField.PointType.Player, longName = "Zombie Spawn Points")
	public PointIterator zombieSpawnPoints;


	public PlagueArena(String name, String author, String description, String worldId, String id) {
		super(name, author, description, worldId, id);
	}

	public PlagueArena(String name, String author, String description, World world) {
		super(name, author, description, world);
	}
}
