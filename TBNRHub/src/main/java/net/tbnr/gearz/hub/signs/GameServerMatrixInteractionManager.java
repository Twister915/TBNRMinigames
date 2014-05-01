package net.tbnr.gearz.hub.signs;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import lombok.Data;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.activerecord.GModel;
import net.tbnr.gearz.arena.Point;
import net.tbnr.gearz.game.MinigameMeta;
import net.tbnr.gearz.hub.TBNRHub;
import net.tbnr.util.command.TCommand;
import net.tbnr.util.command.TCommandHandler;
import net.tbnr.util.command.TCommandSender;
import net.tbnr.util.command.TCommandStatus;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Data
public final class GameServerMatrixInteractionManager implements TCommandHandler {
    private final GameServerSignManager manager;

    @TCommand(
            name = "addserversigns",
            permission = "gearz.set-server-sign",
            senders = {TCommandSender.Player},
            usage = "<minigame key>",
            description = "Command to set server signs"
    )
    public TCommandStatus setServerSign(org.bukkit.command.CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length < 1) return TCommandStatus.FEW_ARGS;
        //TODO get a selection
        Vector p1 = null;
        Vector p2 = null;

        //TODO check for intersections

        String key = args[0];

        MinigameMeta metaForKey = getMetaForKey(key);
        if (metaForKey == null) return TCommandStatus.INVALID_ARGS;

        DBCollection hub_sign_matrices = getCollection();
        DBObject pointOneObject = null; //= ArenaFieldSerializer.POINT.getDelegate().getObjectFor(new Point(p1.getX(), p1.getY(), p1.getZ()));
        DBObject pointTwoObject = null; //ArenaFieldSerializer.POINT.getDelegate().getObjectFor(new Point(p2.getX(), p2.getY(), p2.getZ()));
        DBObject object = BasicDBObjectBuilder.start("minigame_key", key).add("bound1", pointOneObject).add("bound2", pointTwoObject).add("world", ((Player)sender).getWorld()).get();
        hub_sign_matrices.save(object);
        loadAndAddMatrixFromDB(object);
        return TCommandStatus.SUCCESSFUL;
    }

    private void loadAndAddMatrixFromDB(DBObject object) {
        DBObject bound1 = (DBObject) object.get("bound1");
        DBObject bound2 = (DBObject) object.get("bound2");
        String key = (String) object.get("minigame_key");
        MinigameMeta metaForKey = getMetaForKey(key);
        Point bound1Point = null;//= (Point) ArenaFieldSerializer.POINT.getDelegate().getObjectFor(bound1);
        Point bound2Point = null;//= (Point) ArenaFieldSerializer.POINT.getDelegate().getObjectFor(bound2);
        World world = Bukkit.getWorld((String) object.get("world"));
        GameServerMatrix gameServerMatrix = new GameServerMatrix(
                new Vector(bound1Point.getX(), bound1Point.getY(), bound1Point.getZ()),
                new Vector(bound2Point.getX(), bound2Point.getY(), bound2Point.getZ()),
                world,
                metaForKey
        );
        this.manager.addGameServerMatrix(gameServerMatrix);
    }

    public void loadAllFromDatabase() {
        DBCursor dbObjects = getCollection().find();
        for (DBObject dbObject : dbObjects) {
            try {
                loadAndAddMatrixFromDB(dbObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static DBCollection getCollection() {
        return TBNRHub.getInstance().getMongoDB().getCollection("hub_sign_matrices");
    }

    private static MinigameMeta getMetaForKey(String key) {
        MinigameMeta minigameMeta = null;//new MinigameMeta(TBNRHub.getInstance().getMongoDB(), key);
        GModel one = minigameMeta.findOne();
        if (one == null) return null;
        return (MinigameMeta) one;
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.handleCommandStatus(status, sender);
    }
}
