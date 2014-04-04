package net.tbnr.gearz.cm;/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.PointIterator;
import org.bukkit.World;

/**
 * <p/>
 * Latest Change:
 * <p/>
 *
 * @author George
 * @since 04/04/14
 */
public class CatAndMouseArena extends Arena {
	@ArenaField(key = "spawn-points", loop = true, type = ArenaField.PointType.Player, longName = "Spawn Points")
	public PointIterator SpawnPoints;

	public CatAndMouseArena(String name, String author, String description, String worldId, String id) {
		super(name, author, description, worldId, id);
	}

	public CatAndMouseArena(String name, String author, String description, World world) {
		super(name, author, description, world);
	}
}
