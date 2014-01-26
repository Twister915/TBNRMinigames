package net.tbnr.commerce.items;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.tbnr.commerce.items.definitions.RoseOfDeath;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.player.TPlayer;
import net.tbnr.util.player.TPlayerJoinEvent;
import net.tbnr.util.player.TPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import javax.management.ReflectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SuppressWarnings("unchecked")
public final class CommerceItemManager implements Listener, CommerceItemAPI {
    private HashMap<GearzPlayer, PlayerCommerceItems> playerCommerceData;
    private static final String dbListKey = "commerce_purchases";
    private static Class[] items;
    public CommerceItemManager() {
        this.playerCommerceData = new HashMap<>();
        items = new Class[]{RoseOfDeath.class};
        reloadPlayers();
    }
    @Override
    public void reloadPlayer(GearzPlayer player) {
        BasicDBList commerce_purchases = getPurchaseList(player);
        if (this.playerCommerceData.containsKey(player)) {
            for (CommerceItem commerceItem : this.playerCommerceData.get(player).getItems()) {
                HandlerList.unregisterAll(commerceItem);
            }
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
            CommerceItem magic = constructCommerceItem(key, player);
            magic.register();
            magic.registered();
            items.add(magic);
        }
        this.playerCommerceData.put(player, new PlayerCommerceItems(player, items));
    }

    private BasicDBList getPurchaseList(GearzPlayer player) {
        DBObject playerDocument = player.getTPlayer().getPlayerDocument();
        BasicDBList commerce_purchases;
        try {
            commerce_purchases = (BasicDBList) playerDocument.get(CommerceItemManager.dbListKey);
        } catch (ClassCastException ex) {
            commerce_purchases = new BasicDBList();
        }
        return commerce_purchases;
    }
    @Override
    public void reloadPlayers() {
        this.playerCommerceData = new HashMap<>();
        for (TPlayer tPlayer : TPlayerManager.getInstance().getPlayers()) {
            reloadPlayer(GearzPlayer.playerFromTPlayer(tPlayer));
        }
    }

    @Override
    public void revokeItem(GearzPlayer player, CommerceItem item) {
        revokeItem(player, item.getClass());
    }

    @Override
    public void revokeItem(GearzPlayer player, Class<? extends CommerceItem> item) {
        if (!playerHasItem(player, item)) return;
        BasicDBList purchaseList = getPurchaseList(player);
        purchaseList.remove(getMetaFor(item).key());
        DBObject playerDocument = player.getTPlayer().getPlayerDocument();
        playerDocument.put(dbListKey, purchaseList);
        player.getTPlayer().save();
    }

    @Override
    public List<CommerceItem> getItemsFor(GearzPlayer player) {
        return this.playerCommerceData.get(player).getItems();
    }

    private CommerceItem constructCommerceItem(String key, GearzPlayer player) {
        for (Class clazz : getCommerceItems()) {
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
        reloadPlayer(GearzPlayer.playerFromTPlayer(event.getPlayer()));
    }

    @Override
    public void givePlayerItem(GearzPlayer player, Class<? extends CommerceItem> clazz) {
        BasicDBList purchaseList = getPurchaseList(player);
        purchaseList.add(BasicDBObjectBuilder.start().add("key", getMetaFor(clazz).key()).add("datettime", new Date()).get());
        DBObject playerDocument = player.getTPlayer().getPlayerDocument();
        playerDocument.put(dbListKey, purchaseList);
        player.getTPlayer().save();
    }

    @Override
    public boolean playerHasItem(GearzPlayer player, Class<? extends CommerceItem> clazz) {
        PlayerCommerceItems playerCommerceItems = this.playerCommerceData.get(player);
        for (CommerceItem commerceItem : playerCommerceItems.getItems()) {
            if (commerceItem.getClass().equals(clazz)) return true;
        }
        return false;
    }

    @Override
    public Class<? extends CommerceItem> getItemForID(String key) {
        for (Class clazz : items) {
            CommerceItemMeta meta;
            try {
                meta = getMetaFor(clazz);
            } catch (RuntimeException ex) {
                continue;
            }
            if (meta.key().equals(key)) {
                return clazz;
            }
        }
        return null;
    }

    @Override
    public List<Class<? extends CommerceItem>> getCommerceItems() {
        List<Class> classes = Arrays.asList(items);
        List<Class<? extends CommerceItem>> items = new ArrayList<>();
        for (Class aClass : classes) {
            if (aClass.isAssignableFrom(CommerceItem.class)) items.add(aClass);
        }
        return items;
    }

    @Override
    public CommerceItemMeta getMetaFor(Class<? extends CommerceItem> clazz) {
        Annotation annotation = clazz.getAnnotation(CommerceItemMeta.class);
        if (annotation == null) throw new RuntimeException("could not find meta!");
        return (CommerceItemMeta) annotation;
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
