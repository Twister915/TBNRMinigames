package net.tbnr.mc.hub.signs;

import com.mongodb.*;
import lombok.Data;
import net.cogzmc.engine.gearz.Gearz;
import net.cogzmc.engine.gearz.arena.ArenaFieldSerializer;
import net.cogzmc.engine.gearz.arena.Point;
import net.cogzmc.engine.gearz.game.MinigameMeta;
import net.cogzmc.engine.util.command.TCommand;
import net.cogzmc.engine.util.command.TCommandHandler;
import net.cogzmc.engine.util.command.TCommandSender;
import net.cogzmc.engine.util.command.TCommandStatus;
import net.cogzmc.engine.util.player.TPlayerDisconnectEvent;
import net.tbnr.mc.hub.TBNRHub;
import net.tbnr.mc.manager.TBNRNetworkManager;
import net.tbnr.mc.manager.TBNRPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

@Data
public final class GameServerMatrixInteractionManager implements TCommandHandler, Listener {
    private final GameServerSignManager manager;

    private final Map<TBNRPlayer, Vector> selectionPointOne = new HashMap<>();
    private final Map<TBNRPlayer, Vector> selectionPointTwo = new HashMap<>();

    @TCommand(
            name = "addserversigns",
            permission = "gearz.set-server-sign",
            senders = {TCommandSender.Player},
            usage = "<minigame key>",
            description = "Command to set server signs"
    )
    public TCommandStatus setServerSign(org.bukkit.command.CommandSender sender, TCommandSender type, TCommand meta, Command command, String[] args) {
        if (args.length < 1) return TCommandStatus.FEW_ARGS;
        TBNRPlayer player = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer((Player) sender);
        Vector p1 = this.selectionPointOne.get(player);
        Vector p2 = this.selectionPointTwo.get(player);

        String key = args[0];

        MinigameMeta metaForKey = getMetaForKey(key);
        if (metaForKey == null) return TCommandStatus.INVALID_ARGS;

        DBCollection hub_sign_matrices = getCollection();
        DBObject pointOneObject = ArenaFieldSerializer.POINT.getDelegate().getObjectFor(new Point(p1.getX(), p1.getY(), p1.getZ()));
        DBObject pointTwoObject  = ArenaFieldSerializer.POINT.getDelegate().getObjectFor(new Point(p2.getX(), p2.getY(), p2.getZ()));
        DBObject object = BasicDBObjectBuilder.start("minigame_key", key).add("bound1", pointOneObject).add("bound2", pointTwoObject).add("world", ((Player)sender).getWorld().getName()).get();
        hub_sign_matrices.save(object);
        loadAndAddMatrixFromDB(object);
        return TCommandStatus.SUCCESSFUL;
    }

    private void loadAndAddMatrixFromDB(DBObject object) {
        DBObject bound1 = (DBObject) object.get("bound1");
        DBObject bound2 = (DBObject) object.get("bound2");
        String key = (String) object.get("minigame_key");
        MinigameMeta metaForKey = getMetaForKey(key);
        Point bound1Point = (Point) ArenaFieldSerializer.POINT.getDelegate().getObjectFor(bound1);
        Point bound2Point = (Point) ArenaFieldSerializer.POINT.getDelegate().getObjectFor(bound2);
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
        DBObject one = TBNRHub.getInstance().getMongoDB().getCollection("minigamemetas").findOne(new BasicDBObject("key", key));
        if (one == null) return null;
        return new MinigameMeta(TBNRHub.getInstance().getMongoDB(), one);
    }

    @Override
    public void handleCommandStatus(TCommandStatus status, CommandSender sender, TCommandSender senderType) {
        Gearz.handleCommandStatus(status, sender);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.BLAZE_ROD) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        event.getPlayer().sendMessage(ChatColor.GREEN + "Set point in selection!");
        TBNRPlayer playerFromPlayer = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(event.getPlayer());
        Vector vector = event.getClickedBlock().getLocation().toVector();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            this.selectionPointTwo.put(playerFromPlayer, vector);
        } else {
            this.selectionPointOne.put(playerFromPlayer, vector);
        }
     }

    @EventHandler
    public void onPlayerDisconnect(TPlayerDisconnectEvent event) {
        TBNRPlayer playerFromPlayer = TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromTPlayer(event.getPlayer());
        if (this.selectionPointOne.containsKey(playerFromPlayer)) this.selectionPointOne.remove(playerFromPlayer);
        if (this.selectionPointTwo.containsKey(playerFromPlayer)) this.selectionPointTwo.remove(playerFromPlayer);
    }
}
