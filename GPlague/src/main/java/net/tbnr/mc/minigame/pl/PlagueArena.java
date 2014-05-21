/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.mc.minigame.pl;

import net.cogzmc.engine.gearz.arena.Arena;
import net.cogzmc.engine.gearz.arena.ArenaField;
import net.cogzmc.engine.gearz.arena.PointIterator;
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
