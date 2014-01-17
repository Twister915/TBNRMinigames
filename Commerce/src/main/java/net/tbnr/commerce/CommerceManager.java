package net.tbnr.commerce;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.tbnr.commerce.items.RoseOfDeath;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.player.TPlayer;
import net.tbnr.util.player.TPlayerJoinEvent;
import net.tbnr.util.player.TPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class CommerceManager implements Listener {
    private HashMap<GearzPlayer, PlayerCommerceItems> playerCommerceData;
    private static Class[] items;
    public CommerceManager() {
        this.playerCommerceData = new HashMap<>();
        items = new Class[]{RoseOfDeath.class};
        reloadPlayers();
    }
    public void reloadPlayer(GearzPlayer player) {
        DBObject playerDocument = player.getTPlayer().getPlayerDocument();
        BasicDBList commerce_purchases;
        try {
           commerce_purchases = (BasicDBList) playerDocument.get("commerce_purchases");
        } catch (ClassCastException ex) {
           commerce_purchases = new BasicDBList();
        }
        List<CommerceItem> items = new ArrayList<>();
        for (Object commerce_purchase : commerce_purchases) {
            if (!(commerce_purchase instanceof DBObject)) continue;
            DBObject object = (DBObject)commerce_purchase;
            String key;
            try {
                key = (String) object.get("key");
            } catch (ClassCastException ex) {
                continue;
            }
            CommerceItem magic = magic(key, player);
            items.add(magic);
        }
        this.playerCommerceData.put(player, new PlayerCommerceItems(player, items));
    }
    public void reloadPlayers() {
        this.playerCommerceData = new HashMap<>();
        for (TPlayer tPlayer : TPlayerManager.getInstance().getPlayers()) {
            reloadPlayer(GearzPlayer.playerFromTPlayer(tPlayer));
        }
    }
    private CommerceItem magic(String key, GearzPlayer player) {
        for (Class clazz : items) {
            if (!clazz.isAssignableFrom(CommerceItem.class)) continue;
            CommerceItemMeta meta = (CommerceItemMeta) clazz.getAnnotation(CommerceItemMeta.class);
            if (meta == null) continue;
            if (meta.key().equals(key)) {
                CommerceItem item;
                try {
                    item = (CommerceItem) clazz.getConstructor(GearzPlayer.class).newInstance(player);
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                return item;
            }
        }
        return null;
    }
    @EventHandler
    public void onPlayerJoin(TPlayerJoinEvent event) {
    }
    @Data
    @RequiredArgsConstructor
    public static class PlayerCommerceItems {
        @NonNull
        private GearzPlayer player;
        @NonNull
        private List<CommerceItem> items;
    }
}
