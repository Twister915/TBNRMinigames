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

package net.tbnr.mc.minigame.db;

import net.cogzmc.engine.gearz.arena.Arena;
import net.cogzmc.engine.gearz.arena.ArenaField;
import net.cogzmc.engine.gearz.arena.PointIterator;
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
