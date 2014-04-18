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

package net.tbnr.minigame.predator;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.PointIterator;
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
    public PointIterator predatorSpawn;

    @ArenaField(key = "spawn-points", loop = true, type = ArenaField.PointType.Player, longName = "Spawn Points")
    public PointIterator spawnPoints;

    public PredatorArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public PredatorArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }
}
