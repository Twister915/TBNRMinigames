/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.minigame.ascension;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.PointIterator;
import org.bukkit.World;

public final class ASArena extends Arena {
    @ArenaField(longName = "Spawn Points", key = "spawn-points", loop = true, type = ArenaField.PointType.Player)
    public PointIterator spawnPoints;

    public ASArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public ASArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }
}
