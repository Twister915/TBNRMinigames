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

package net.tbnr.gearz.hh;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.PointIterator;
import org.bukkit.World;

/**
 * HeadHunter Arena
 */
public class HeadHunterArena extends Arena {

    @ArenaField(key = "spawn-points", loop = true, type = ArenaField.PointType.Player, longName = "Spawn Points")
    public PointIterator spawnPoints;

    public HeadHunterArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public HeadHunterArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }
}

