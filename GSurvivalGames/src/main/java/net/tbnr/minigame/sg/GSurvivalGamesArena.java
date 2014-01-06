package net.tbnr.minigame.sg;

import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.arena.ArenaField;
import net.tbnr.gearz.arena.ArenaIterator;
import net.tbnr.gearz.arena.Point;
import org.bukkit.World;


public class GSurvivalGamesArena extends Arena {

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
