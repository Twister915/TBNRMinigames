package net.tbnr.minigame.sg;

import net.tbnr.gearz.arena.*;
import org.bukkit.World;

@RequiresRegion
public final class GSurvivalGamesArena extends Arena {

    @ArenaField(key = "cornicopia-points", type = ArenaField.PointType.Player, longName = "Cornicopia Spawn Points", loop = true)
    public PointIterator cornicopiaPoints;

    @ArenaField(key = "tierone-chests", type = ArenaField.PointType.Block, longName = "Tier One Chests", loop = false)
    public PointIterator tierOneChests;

    @ArenaField(key = "tiertwo-chests", type = ArenaField.PointType.Block, longName = "Tier Two Chests", loop = false)
    public PointIterator tierTwoChests;

    @ArenaField(key = "cornicopia-chests", type = ArenaField.PointType.Block, longName = "Cornicopia Chests", loop = false)
    public PointIterator cornicopiaChests;

    @ArenaField(key = "tierthree-chests", type = ArenaField.PointType.Block, longName = "Tier Three", loop = false)
    public PointIterator tierThreeChests;

    public GSurvivalGamesArena(String name, String author, String description, String worldId, String id) {
        super(name, author, description, worldId, id);
    }

    public GSurvivalGamesArena(String name, String author, String description, World world) {
        super(name, author, description, world);
    }
}
