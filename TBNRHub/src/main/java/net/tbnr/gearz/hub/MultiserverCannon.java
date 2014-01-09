package net.tbnr.gearz.hub;

import net.tbnr.gearz.effects.GearzLabelEntity;
import net.tbnr.util.TPlugin;
import net.tbnr.util.player.TPlayer;
import net.tbnr.util.player.TPlayerDisconnectEvent;
import net.tbnr.util.player.TPlayerJoinEvent;
import net.tbnr.util.player.TPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 8/30/13
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiserverCannon implements ConfigurationSerializable, Listener {
    private final String server;
    private final String referenceBlock;
    private final String refrenceLook;
    private final HashMap<TPlayer, GearzLabelEntity> labels;

    public MultiserverCannon(String server, String referenceBlock, String refrenceLook) {
        this.server = server;
        this.referenceBlock = referenceBlock;
        this.refrenceLook = refrenceLook;
        this.labels = new HashMap<>();
        for (TPlayer player : TPlayerManager.getInstance().getPlayers()) {
            label(player);
        }
    }

    public void connecting(TPlayer player) {
        this.labels.get(player).updateTag(TBNRHub.getInstance().getFormat("formats.connecting", false, new String[]{"<server>", server}));
    }

    @EventHandler()
    @SuppressWarnings("unused")
    public void onPlayerJoin(final TPlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(TBNRHub.getInstance(), new Runnable() {
            @Override
            public void run() {
                label(event.getPlayer());
            }
        }, 5);
    }

    private void label(TPlayer player) {
        this.labels.put(player, new GearzLabelEntity(player.getPlayer(), TBNRHub.getInstance().getFormat("formats.server-label", false, new String[]{"<server>", this.server}), this.getRefrenceBlock().add(0, -0.4, 1)));

    }

    public void removeLabelAll() {
        for (Map.Entry<TPlayer, GearzLabelEntity> tPlayerGearzLabelEntityEntry : this.labels.entrySet()) {
            tPlayerGearzLabelEntityEntry.getValue().destroy();
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void disconnect(TPlayerDisconnectEvent event) {
        //labels.get(event.getPlayer()).destroy();
        labels.remove(event.getPlayer());
    }

    public String getServer() {
        return server;
    }

    public Location getRefrenceBlock() {
        return TPlugin.parseLocationString(referenceBlock);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> stuff = new HashMap<>();
        stuff.put("server", this.server);
        stuff.put("referenceBlock", this.referenceBlock);
        stuff.put("refrenceLook", this.refrenceLook);
        return stuff;
    }

    @SuppressWarnings("UnusedDeclaration")
    public MultiserverCannon(Map<String, Object> map) {
        this((String) map.get("server"), (String) map.get("referenceBlock"), (String) map.get("refrenceLook"));
    }

    public Location getRefrenceLook() {
        return TPlugin.parseLocationString(refrenceLook);
    }
}