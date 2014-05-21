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

package net.tbnr.mc.minigame.eg;

import net.cogzmc.engine.gearz.arena.Arena;
import net.cogzmc.engine.gearz.arena.ArenaField;
import net.cogzmc.engine.gearz.arena.PointIterator;
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
