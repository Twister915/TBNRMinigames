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

package net.tbnr.gearz.hub;

import net.tbnr.gearz.arena.*;
import org.bukkit.World;

@ArenaCollection(collection = "hub_arena")
public class HubArena extends Arena {

    public HubArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public HubArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }

    @ArenaField(type = ArenaField.PointType.Player, loop = true, key = "spawn-points", longName = "Spawn Points")
    public ArenaIterator<Point> spawnPoints;
}
