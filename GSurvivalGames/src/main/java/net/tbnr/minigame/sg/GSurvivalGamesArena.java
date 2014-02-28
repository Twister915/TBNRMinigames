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

package net.tbnr.minigame.sg;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import org.bukkit.World;


public final class GSurvivalGamesArena extends Arena {

    @ArenaField(key = "cornicopia-points", type = ArenaField.PointType.Player, longName = "Cornicopia Spawn Points", loop = true)
    public ArenaIterator<Point> cornicopiaPoints;

    @ArenaField(key = "tierone-chests", type = ArenaField.PointType.Block, longName = "Tier One Chests", loop = false)
    public ArenaIterator<Point> tierOneChests;

    @ArenaField(key = "tiertwo-chests", type = ArenaField.PointType.Block, longName = "Tier Two Chests", loop = false)
    public ArenaIterator<Point> tierTwoChests;

    @ArenaField(key = "cornicopia-chests", type = ArenaField.PointType.Block, longName = "Cornicopia Chests", loop = false)
    public ArenaIterator<Point> cornicopiaChests;

    @ArenaField(key = "tierthree-chests", type = ArenaField.PointType.Block, longName = "Tier Three", loop = false)
    public ArenaIterator<Point> tierThreeChests;

    public GSurvivalGamesArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public GSurvivalGamesArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }
}
